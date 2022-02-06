package couch.camping.controller.review;

import couch.camping.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
//
//    @PostMapping("/camps/{campId}/reviews")
//    public ResponseEntity write(@PathVariable Long campId) {
//
//
//
//    }

}
