package couch.camping.controller.member;

import couch.camping.controller.member.dto.response.MemberReviewsResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.review.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberReviewController {

    private final ReviewService reviewService;

    //회원이 작성한 리뷰 조회
    @ApiOperation(value = "회원이 작성한 리뷰 조회 API", notes = "Header 의 토큰에 해당하는 회원이 작성한 리뷰를 조회합니다.")
    @GetMapping("/me/reviews")
    public ResponseEntity<Page<MemberReviewsResponseDto>> getMemberReviews(Pageable pageable, Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();

        return ResponseEntity.ok(reviewService
                .retrieveMemberReviews(memberId, pageable).map(review-> new MemberReviewsResponseDto(review)));
    }

}
