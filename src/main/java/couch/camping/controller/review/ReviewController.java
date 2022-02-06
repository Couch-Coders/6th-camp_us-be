package couch.camping.controller.review;

import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.camp.service.CampService;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.review.entity.Review;
import couch.camping.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final CampService campService;
    private final ReviewService reviewService;

    @PostMapping("/camps/{campId}/reviews")
    public ResponseEntity<ReviewWriteResponseDto> write(@PathVariable Long campId
            , @RequestBody ReviewWriteRequestDto reviewWriteRequestDto, Authentication authentication) {

        Review review = reviewService.write(campId, (Member) authentication.getPrincipal(), reviewWriteRequestDto);
        return new ResponseEntity(new ReviewWriteResponseDto(review), HttpStatus.CREATED);

    }

}
