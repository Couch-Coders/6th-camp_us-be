package couch.camping.controller.camp;


import com.google.firebase.auth.FirebaseAuthException;
import couch.camping.controller.review.dto.response.ReviewImageUrlResponseDto;
import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewRetrieveResponseDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.review.service.ReviewService;
import couch.camping.util.RequestUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/camps")
public class CampReviewController {

    private final ReviewService reviewService;

    //리뷰 작성
    @ApiOperation(value = "리뷰 작성 API", notes = "캠핑장에 리뷰를 작성합니다.")
    @PostMapping("/{campId}/reviews")
    public ResponseEntity<ReviewWriteResponseDto> writeReview(
            @ApiParam(value = "리뷰 ID", required = true) @PathVariable Long campId,
            @ApiParam(value = "리뷰 작성 (요청 바디)", required = true) @RequestBody @Valid ReviewWriteRequestDto reviewWriteRequestDto,
            Authentication authentication) {

        Member member = (Member) authentication.getPrincipal();
        ReviewWriteResponseDto responseDto = reviewService.write(campId, member, reviewWriteRequestDto);

        return new ResponseEntity(responseDto, HttpStatus.CREATED);
    }

    //리뷰 조회
    @ApiOperation(value = "리뷰 조회 API", notes = "캠핑장 ID 에 해당하는 리뷰 조회 및 페이징. 쿼리스트링 예시(?page=0&size=10")
    @GetMapping("/{campId}/reviews")
    public ResponseEntity<Page<ReviewRetrieveResponseDto>> getReviewList(Pageable pageable,
                                                                         @ApiParam(value = "캠핑장 ID", required = true) @PathVariable Long campId,
                                                                         HttpServletRequest request) throws FirebaseAuthException {
        String header = RequestUtil.getAuthorizationToken(request);
        return ResponseEntity.ok(reviewService.retrieveAll(campId, pageable, header));
    }

    //캠핑장의 리뷰 이미지 조회
    @ApiOperation(value = "캠핑장의 리뷰 이미지 조회 API", notes = "캠핑장 ID 에 해당하는 리뷰 이미지 조회 및 페이징. 쿼리스트링 예시(?page=0&size=10")
    @GetMapping("/{campId}/reviews/images")
    public ResponseEntity<Page<ReviewImageUrlResponseDto>> getReviewImageUrlList(@PathVariable Long campId, Pageable pageable) {
        return ResponseEntity.ok(reviewService.retrieveAllImageUrl(campId, pageable));
    }
}
