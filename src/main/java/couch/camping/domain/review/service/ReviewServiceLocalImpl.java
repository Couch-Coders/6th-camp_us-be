package couch.camping.domain.review.service;

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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Profile("local")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewServiceLocalImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final CampRepository campRepository;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public ReviewWriteResponseDto write(Long campId, Member member, ReviewWriteRequestDto reviewWriteRequestDto) {

        Camp findCamp = campRepository.findById(campId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_CAMP, "캠핑장 ID 에 해당하는 캠핑장이 없습니다.");
                });

        Review review = Review.builder()
                .member(member)
                .camp(findCamp)
                .imgUrl(reviewWriteRequestDto.getImgUrl())
                .content(reviewWriteRequestDto.getContent())
                .rate(reviewWriteRequestDto.getRate())
                .build();

        return new ReviewWriteResponseDto(reviewRepository.save(review));
    }

    @Override
    public Page<ReviewRetrieveResponseDto> retrieveAll(Long campId, Pageable pageable, String header) {
        if (header == null) {
            return reviewRepository.findByCampId(pageable, campId)
                    .map(review -> new ReviewRetrieveResponseDto(review));
        } else {
            Member member;
            try {
                member = (Member)userDetailsService.loadUserByUsername(header);
            } catch (UsernameNotFoundException e) {
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

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Member member) {

        try {
            reviewRepository.deleteById(reviewId);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException(ErrorCode.NOT_FOUND_REVIEW, "리뷰 ID 에 맞는 리뷰가 없습니다.");
        }
    }

    @Override
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

    @Override
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

    public Page<ReviewRetrieveResponseDto> getBestReviews(Pageable pageable) {
        return reviewRepository.findAllByLikeCntGreaterThan(pageable).map(review -> new ReviewRetrieveResponseDto(review));
    }

    @Override
    public long countMemberReviews(Long memberId) {
        return reviewRepository.countByMemberId(memberId);
    }

    @Override
    public Page<Review> retrieveMemberReviews(Long memberId, Pageable pageable) {
        return reviewRepository.findByMemberId(pageable, memberId);
    }
}
