package couch.camping.controller.review;

import com.google.firebase.auth.FirebaseAuthException;
import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewRetrieveResponseDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.review.service.ReviewService;
import couch.camping.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    
    //리뷰 작성
    @PostMapping("/camps/{campId}/reviews")
    public ResponseEntity<ReviewWriteResponseDto> writeReview(
            @PathVariable Long campId,
            @RequestBody ReviewWriteRequestDto reviewWriteRequestDto,
            Authentication authentication) {

        Member member = (Member) authentication.getPrincipal();
        ReviewWriteResponseDto responseDto = reviewService.write(campId, member, reviewWriteRequestDto);

        return new ResponseEntity(responseDto, HttpStatus.CREATED);
    }

    //리뷰 조회
    @GetMapping("/camps/{campId}/reviews")
    public ResponseEntity<Page<ReviewRetrieveResponseDto>> getReviewList(Pageable pageable,
                                                                         @PathVariable Long campId,
                                                                         HttpServletRequest request) throws FirebaseAuthException {
        String header = RequestUtil.getAuthorizationToken(request);
        return ResponseEntity.ok(reviewService.retrieveAll(campId, pageable, header));
    }

    //리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity editReview(@PathVariable Long reviewId,
                                     @RequestBody ReviewWriteRequestDto reviewWriteRequestDto,
                                     Authentication authentication) {

        Member member = (Member) authentication.getPrincipal();
        ReviewWriteResponseDto responseDto = reviewService.editReview(reviewId, reviewWriteRequestDto, member);

        return ResponseEntity.ok(responseDto);
    }
    
    //리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity deleteReview(@PathVariable Long reviewId, Authentication authentication) {

        Member member = (Member) authentication.getPrincipal();
        reviewService.deleteReview(reviewId, member);

        return ResponseEntity.noContent().build();
    }
    
    //리뷰 좋아요
    @PatchMapping("/reviews/{reviewId}/like")
    public ResponseEntity likeReview(@PathVariable Long reviewId, Authentication authentication) {

        reviewService.likeReview(reviewId, (Member)authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }
    
    //bset 리뷰
    @GetMapping("/reviews/best")
    public ResponseEntity getBestReviews() {
        Page<ReviewRetrieveResponseDto> responseDto = reviewService.getBestReviews();

        return ResponseEntity.ok(responseDto);
    }

}
