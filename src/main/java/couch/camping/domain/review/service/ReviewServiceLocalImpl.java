package couch.camping.domain.review.service;

import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewImageUrlResponseDto;
import couch.camping.controller.review.dto.response.ReviewRetrieveLoginResponse;
import couch.camping.controller.review.dto.response.ReviewRetrieveResponseDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.CampRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.notification.entity.Notification;
import couch.camping.domain.notification.repository.NotificationRepository;
import couch.camping.domain.review.entity.Review;
import couch.camping.domain.review.repository.ReviewRepository;
import couch.camping.domain.reviewlike.entity.ReviewLike;
import couch.camping.domain.reviewlike.repository.ReviewLikeRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public ReviewWriteResponseDto write(Long campId, Member member, ReviewWriteRequestDto reviewWriteRequestDto) {

        Camp findCamp = campRepository.findById(campId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_CAMP, "????????? ID ??? ???????????? ???????????? ????????????.");
                });

        Review review = Review.builder()
                .member(member)
                .camp(findCamp)
                .imgUrl(reviewWriteRequestDto.getImgUrl())
                .content(reviewWriteRequestDto.getContent())
                .rate(reviewWriteRequestDto.getRate())
                .build();

        Review saveReview = reviewRepository.save(review);
        findCamp.increaseRate(reviewWriteRequestDto.getRate());
        return new ReviewWriteResponseDto(saveReview);
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
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "????????? ???????????? ????????? ???????????? ????????????.");
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
        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_REVIEW, "?????? ID ??? ?????? ????????? ????????????.");
                });

        if (findReview.getMember() != member) {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "?????? ????????? ????????? ????????????.");
        }

        findReview.getCamp().decreaseRate(findReview.getRate());
        reviewRepository.deleteById(reviewId);
    }

    @Override
    @Transactional
    public ReviewWriteResponseDto editReview(Long reviewId, ReviewWriteRequestDto reviewWriteRequestDto, Member member) {

        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_REVIEW, "?????? ID ??? ?????? ????????? ????????????.");
                });

        if (findReview.getMember() != member) {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "?????? ????????? ????????? ????????????.");
        }

        findReview.getCamp().editDate(findReview.getRate(), reviewWriteRequestDto.getRate());

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
                    throw new CustomException(ErrorCode.NOT_FOUND_REVIEW, "?????? ID ??? ?????? ????????? ????????????.");
                });
        
        Optional<ReviewLike> optionalReviewLike = reviewLikeRepository
                .findByReviewIdAndMemberId(reviewId, member.getId());

        boolean present = optionalReviewLike.isPresent();//????????? ????????? ???????????? ???????????? ??????
        
        if (present) {//???????????? ?????? ????????? ??? 1 ??????
            findReview.decreaseLikeCnt();
            reviewLikeRepository.deleteById(optionalReviewLike.get().getId());//reviewLike ????????? ??????
        } else {//???????????? ????????? ???????????? ????????? ????????? ??? 1 ??????
            findReview.increaseLikeCnt();

            ReviewLike reviewLike = new ReviewLike(member);
            reviewLike.addReview(findReview);

            reviewLikeRepository.save(reviewLike);

            if (findReview.getMember() != member) {
                Optional<Notification> optionalNotification = notificationRepository
                        .findByMemberIdAndReviewId(member.getId(), reviewId);//???????????? ?????? ??????, ??????

                if (!optionalNotification.isPresent()) {
                    Notification notification = Notification.builder()
                            .review(findReview)//?????? ?????????
                            .member(member)//???????????? ?????? ?????? ?????????
                            .ownerMember(findReview.getMember())//???????????? ??????
                            .build();

                    notificationRepository.save(notification);
                }
            }
        }
    }

    public Page<ReviewRetrieveResponseDto> getBestReviews(Pageable pageable) {
        return reviewRepository.findAllBestReview(pageable)
                .map(review -> new ReviewRetrieveResponseDto(review));
    }

    @Override
    public long countMemberReviews(Long memberId) {
        return reviewRepository.countByMemberId(memberId);
    }

    @Override
    public Page<Review> retrieveMemberReviews(Long memberId, Pageable pageable) {
        return reviewRepository.findByMemberId(pageable, memberId);
    }

    @Override
    public List<ReviewImageUrlResponseDto> retrieveAllImageUrl(Long campId) {
        List<Review> reviews = reviewRepository.findImageUrlByCampId(campId);
        List<ReviewImageUrlResponseDto> list = new ArrayList<>();

        for (Review r : reviews) {
            list.add(new ReviewImageUrlResponseDto(r));
        }
        return list;
    }
}
