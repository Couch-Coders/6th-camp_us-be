package couch.camping.domain.review.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewImageUrlResponseDto;
import couch.camping.controller.review.dto.response.ReviewRetrieveLoginResponse;
import couch.camping.controller.review.dto.response.ReviewRetrieveResponseDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.camp.CampRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.notification.entity.Notification;
import couch.camping.domain.notification.repository.NotificationRepository;
import couch.camping.domain.review.entity.Review;
import couch.camping.domain.review.entity.ReviewLike;
import couch.camping.domain.review.repository.review.ReviewRepository;
import couch.camping.domain.review.repository.review_like.ReviewLikeRepository;
import couch.camping.domain.review.service.ReviewService;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public ReviewWriteResponseDto write(Long campId, Member member, ReviewWriteRequestDto reviewWriteRequestDto) {

        Camp findCamp = findCampByCampId(campId)
                .orElseThrow(() -> throwCustomException(ErrorCode.NOT_FOUND_CAMP, "캠핑장 ID 에 해당하는 캠핑장이 없습니다."));

        Review review = createReview(member, reviewWriteRequestDto, findCamp);

        Review saveReview = saveReview(review);
        findCamp.increaseRate(reviewWriteRequestDto.getRate());
        return new ReviewWriteResponseDto(saveReview);
    }

    @Override
    public Page<ReviewRetrieveResponseDto> retrieveAll(Long campId, Pageable pageable, String header) {
        if (header == null) {
            return findReviewPageByCampId(campId, pageable)
                    .map(review -> new ReviewRetrieveResponseDto(review));
        } else {
            Member member;
            try {
                member = (Member) findUserDetails(getVerifyIdToken(header));
            } catch (UsernameNotFoundException | FirebaseAuthException | IllegalArgumentException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "토큰에 해당하는 회원이 존재하지 않습니다.");
            }
            return findReviewPageByCampId(campId, pageable)
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
        Review findReview = findOptionalReviewByReviewId(reviewId)
                .orElseThrow(() -> throwCustomException(ErrorCode.NOT_FOUND_REVIEW, "리뷰 ID 에 맞는 리뷰가 없습니다."));

        if (!isSameMember(member, findReview)) {
            throwCustomException(ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 리뷰가 아닙니다.");
        }

        findReview.getCamp().decreaseRate(findReview.getRate());
        deleteReviewByReviewId(reviewId);
    }

    @Override
    @Transactional
    public ReviewWriteResponseDto editReview(Long reviewId, ReviewWriteRequestDto reviewWriteRequestDto, Member member) {

        Review findReview = findOptionalReviewByReviewId(reviewId)
                .orElseThrow(() ->
                        throwCustomException(ErrorCode.NOT_FOUND_REVIEW, "리뷰 ID 에 맞는 리뷰가 없습니다."));

        if (!isSameMember(member, findReview)) {
            throwCustomException(ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 리뷰가 아닙니다.");
        }

        findReview.getCamp().editDate(findReview.getRate(), reviewWriteRequestDto.getRate());

        Review editReview = editReview(reviewWriteRequestDto, findReview);

        return new ReviewWriteResponseDto(editReview);
    }

    @Override
    @Transactional
    public void likeReview(Long reviewId, Member member) {

        Review findReview = findOptionalReviewByReviewId(reviewId)
                .orElseThrow(() -> throwCustomException(ErrorCode.NOT_FOUND_REVIEW, "리뷰 ID 에 맞는 리뷰가 없습니다."));

        Optional<ReviewLike> optionalReviewLike = findOptionalReviewLikeByReviewIdAndMemberId(reviewId, member);

        boolean present = optionalReviewLike.isPresent();//회원이 리뷰에 좋아요를 눌렀는지 확인

        if (present) {//눌렀으면 리뷰 좋아요 수 1 감소
            findReview.decreaseLikeCnt();
            deleteByReviewLikeId(optionalReviewLike.get().getId());//reviewLike 엔티티 삭제
        } else {//좋아요를 누르지 않았으면 리뷰의 좋아요 수 1 증가
            findReview.increaseLikeCnt();

            ReviewLike reviewLike = new ReviewLike(member);
            reviewLike.addReview(findReview);

            saveReviewLike(reviewLike);

            if (!isSameMember(member, findReview)) checkToCreateNotification(member, findReview);

        }
    }

    @Override
    public Page<ReviewRetrieveResponseDto> getBestReviews(Pageable pageable) {
        return findAllReviewGoeOneOrderByLikeCnt(pageable)
                .map(review -> new ReviewRetrieveResponseDto(review));
    }

    @Override
    public long countMemberReviews(Long memberId) {
        return countReviewByMemberId(memberId);
    }

    @Override
    public Page<Review> retrieveMemberReviews(Long memberId, Pageable pageable) {
        return findReviewPageByMemberId(memberId, pageable);
    }

    @Override
    public List<ReviewImageUrlResponseDto> retrieveAllImageUrl(Long campId) {
        List<Review> reviews = findReviewListByNotNullImgUrl(campId);
        return reviewMapToReviewImageUrlResponseDto(reviews);
    }

    private void deleteReviewByReviewId(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private Review editReview(ReviewWriteRequestDto reviewWriteRequestDto, Review findReview) {
        Review editReview = findReview.changeReview(
                reviewWriteRequestDto.getContent(),
                reviewWriteRequestDto.getRate(),
                reviewWriteRequestDto.getImgUrl()
        );
        return editReview;
    }

    private FirebaseToken getVerifyIdToken(String header) throws FirebaseAuthException {
        return firebaseAuth.verifyIdToken(header);
    }

    private UserDetails findUserDetails(FirebaseToken decodedToken) {
        return userDetailsService.loadUserByUsername(decodedToken.getUid());
    }

    private void checkToCreateNotification(Member member, Review review) {
        Optional<Notification> optionalNotification = findOptionalNotificationByMemberIdAndReviewId(review.getId(), member);//좋아요를 누른 회원, 리뷰
        if (!optionalNotification.isPresent())
            saveNotification(createNotification(member, review));
    }

    private boolean isSameMember(Member member, Review findReview) {
        return findReview.getMember() == member;
    }

    private Notification createNotification(Member member, Review findReview) {
        Notification notification = Notification.builder()
                .review(findReview)//리뷰 엔티티
                .member(member)//좋아요를 누른 회원 엔티티
                .ownerMember(findReview.getMember())//게시글의 회원
                .build();
        return notification;
    }

    private RuntimeException throwCustomException(ErrorCode errorCode, String message) {
        throw new CustomException(errorCode, message);
    }

    private Page<Review> findReviewPageByCampId(Long campId, Pageable pageable) {
        return reviewRepository.findByCampId(pageable, campId);
    }

    private Optional<Review> findOptionalReviewByReviewId(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    private Optional<ReviewLike> findOptionalReviewLikeByReviewIdAndMemberId(Long reviewId, Member member) {
        return reviewLikeRepository
                .findByReviewIdAndMemberId(reviewId, member.getId());
    }

    private void deleteByReviewLikeId(Long reviewLikeId) {
        reviewLikeRepository.deleteById(reviewLikeId);
    }

    private Optional<Notification> findOptionalNotificationByMemberIdAndReviewId(Long reviewId, Member member) {
        return notificationRepository
                .findByMemberIdAndReviewId(member.getId(), reviewId);
    }

    private Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    private ReviewLike saveReviewLike(ReviewLike reviewLike) {
        return reviewLikeRepository.save(reviewLike);
    }

    private Page<Review> findAllReviewGoeOneOrderByLikeCnt(Pageable pageable) {
        return reviewRepository.findAllGoeOneLikeCntOrderByLikeCnt(pageable);
    }

    private Long countReviewByMemberId(Long memberId) {
        return reviewRepository.countByMemberId(memberId);
    }

    private Page<Review> findReviewPageByMemberId(Long memberId, Pageable pageable) {
        return reviewRepository.findByMemberIdWithPaging(pageable, memberId);
    }

    private List<ReviewImageUrlResponseDto> reviewMapToReviewImageUrlResponseDto(List<Review> reviews) {
        return reviews.stream()
                .map(r -> new ReviewImageUrlResponseDto(r)).collect(Collectors.toList());
    }

    private List<Review> findReviewListByNotNullImgUrl(Long campId) {
        return reviewRepository.findNotNullImgUrlByCampId(campId);
    }

    private Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    private Optional<Camp> findCampByCampId(Long campId) {
        return campRepository.findById(campId);
    }

    private Review createReview(Member member, ReviewWriteRequestDto reviewWriteRequestDto, Camp findCamp) {
        return Review.builder()
                .member(member)
                .camp(findCamp)
                .imgUrl(reviewWriteRequestDto.getImgUrl())
                .content(reviewWriteRequestDto.getContent())
                .rate(reviewWriteRequestDto.getRate())
                .build();
    }
}
