package couch.camping.controller.review;

import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewRetrieveRequestDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.camp.service.CampService;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.review.entity.Review;
import couch.camping.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final CampService campService;
    private final ReviewService reviewService;
    
    //리뷰 작성
    @PostMapping("/camps/{campId}/reviews")
    public ResponseEntity<ReviewWriteResponseDto> writeReview(@PathVariable Long campId
            , @RequestBody ReviewWriteRequestDto reviewWriteRequestDto, Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();
        Review review = reviewService.write(campId, memberId, reviewWriteRequestDto);
        return new ResponseEntity(new ReviewWriteResponseDto(review), HttpStatus.CREATED);

    }
    //리뷰 조회
    @GetMapping("/camps/{campId}/reviews")
    public ResponseEntity getReviewList(@PathVariable Long campId) {

        List<Review> reviews = reviewService.retrieveAll(campId);
        List<ReviewRetrieveRequestDto> reviewsDtoList = new ReviewRetrieveRequestDto().listMapper(reviews);
        return new ResponseEntity(reviewsDtoList, HttpStatus.OK);
    }

}
