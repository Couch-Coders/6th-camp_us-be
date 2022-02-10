package couch.camping.controller.review;

import couch.camping.controller.review.dto.request.ReviewRequestModel;
import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewRetrieveResponseDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.camp.service.CampService;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final CampService campService;
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
    public ResponseEntity<Page<ReviewRetrieveResponseDto>> getReviewList(@Valid ReviewRequestModel reviewRequestModel,
                                                                         @PathVariable Long campId) {

        return ResponseEntity.ok(reviewService.retrieveAll(campId, reviewRequestModel));
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

}
