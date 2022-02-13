package couch.camping.domain.review.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewRetrieveLoginResponse;
import couch.camping.controller.review.dto.response.ReviewRetrieveResponseDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.CampRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.review.entity.Review;
import couch.camping.domain.review.repository.ReviewRepository;
import couch.camping.domain.reviewlike.entity.ReviewLike;
import couch.camping.domain.reviewlike.repository.ReviewLikeRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Profile("prod")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final CampRepository campRepository;
    private final UserDetailsService userDetailsService;
    private final FirebaseAuth firebaseAuth;

    @Transactional
    public ReviewWriteResponseDto write(Long campId, Member member, ReviewWriteRequestDto reviewWriteRequestDto) {

        Camp findCamp = campRepository.findById(campId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_CAMP, "캠핑장 ID 에 해당하는 캠핑장이 없습니다.");
                });


        member.increaseReviewCnt();

        Review review = Review.builder()
                .member(member)
                .camp(findCamp)
                .imgUrl(reviewWriteRequestDto.getImgUrl())
                .content(reviewWriteRequestDto.getContent())
                .rate(reviewWriteRequestDto.getRate())
                .build();

        return new ReviewWriteResponseDto(reviewRepository.save(review));
    }

    public Page<ReviewRetrieveResponseDto> retrieveAll(Long campId, Pageable pageable, String header) {
        if (header == null) {
            return reviewRepository.findByCampId(pageable, campId)
                    .map(review -> new ReviewRetrieveResponseDto(review));
        } else {
            Member member;
            try {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(header);
                member = (Member)userDetailsService.loadUserByUsername(decodedToken.getUid());
            } catch (UsernameNotFoundException | FirebaseAuthException | IllegalArgumentException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "토큰에 해당하는 회원이 존재하지 않습니다.");
            }
            return reviewRepository.findByCampId(pageable, campId)
                    .map(review -> {
                        List<ReviewLike> reviewLikeList = review.getReviewLikeList();

                        for (ReviewLike reviewLike : reviewLikeList) {
                            if (reviewLike.getMember() == member) {
                                return new ReviewRetrieveLoginResponse(review, true);
                            }
                        }
                        return new ReviewRetrieveLoginResponse(review, false);
                    });
        }
    }

    @Transactional
    public void deleteReview(Long reviewId, Member member) {

        try {
            member.decreaseReviewCnt();
            reviewRepository.deleteById(reviewId);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException(ErrorCode.NOT_FOUND_REVIEW, "리뷰 ID 에 맞는 리뷰가 없습니다.");
        }

    }

    @Transactional
    public ReviewWriteResponseDto editReview(Long reviewId, ReviewWriteRequestDto reviewWriteRequestDto, Member member) {

        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_REVIEW, "리뷰 ID 에 맞는 리뷰가 없습니다.");
                });

        if (findReview.getMember().getId() != member.getId()) {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 리뷰가 아닙니다.");
        }

        Review editReview = findReview.changeReview(
                reviewWriteRequestDto.getContent(),
                reviewWriteRequestDto.getRate(),
                reviewWriteRequestDto.getImgUrl()
        );

        return new ReviewWriteResponseDto(editReview);
    }

    @Transactional
    public void likeReview(Long reviewId, Member member) {

        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_REVIEW, "리뷰 ID 에 맞는 리뷰가 없습니다.");
                });
        
        Optional<ReviewLike> reviewLike = reviewLikeRepository.findByReviewIdAndMemberId(reviewId, member.getId());
        boolean present = reviewLike.isPresent();//회원이 리뷰에 좋아요를 눌렀는지 확인
        
        if (present) {//눌렀으면 리뷰 좋아요 수 1 감소
            findReview.decreaseLikeCnt();
            reviewLikeRepository.deleteById(reviewLike.get().getId());//reviewLike 엔티티 삭제
        } else {//좋아료를 누르지 않았으면 리뷰의 좋아요 수 1 증가
            findReview.increaseLikeCnt();
            ReviewLike saveReviewLike = reviewLikeRepository.save(ReviewLike.builder()//reviewLike 엔티티 생성
                    .member(member)
                    .review(findReview)
                    .build());

            findReview.getReviewLikeList().add(saveReviewLike);
        }
    }

    public Page<ReviewRetrieveResponseDto> getBestReviews() {

        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "likeCnt"));
        return reviewRepository.findAllByLikeCntGreaterThan(pageRequest, 1).map(review -> new ReviewRetrieveResponseDto(review));
    }
}
