package couch.camping.domain.review.service;

import com.google.firebase.auth.FirebaseAuthException;
import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewRetrieveResponseDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {


    ReviewWriteResponseDto write(Long campId, Member member, ReviewWriteRequestDto reviewWriteRequestDto);
    Page<ReviewRetrieveResponseDto> retrieveAll(Long campId, Pageable pageable, String header) throws FirebaseAuthException;
    void deleteReview(Long reviewId, Member member);
    ReviewWriteResponseDto editReview(Long reviewId, ReviewWriteRequestDto reviewWriteRequestDto, Member member);
    void likeReview(Long reviewId, Member member);
    Page<ReviewRetrieveResponseDto> getBestReviews(Pageable pageable);
    long countMemberReviews(Long memberId);
    Page<Review> retrieveMemberReviews(Long memberId, Pageable pageable);
}
