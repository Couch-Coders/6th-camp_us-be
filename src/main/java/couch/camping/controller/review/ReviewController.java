package couch.camping.controller.review;

import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewRetrieveResponseDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.review.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 수정
    @ApiOperation(value = "리뷰 수정 API", notes = "리뷰 ID 에 해당하는 리뷰를 수정합니다.(로그인 후 이용가능)")
    @PutMapping("/{reviewId}")
    public ResponseEntity editReview(
            @ApiParam(value = "리뷰 ID", required = true) @PathVariable Long reviewId,
            @ApiParam(value = "리뷰 수정 (요청 바디)", required = true) @RequestBody @Valid ReviewWriteRequestDto reviewWriteRequestDto,
            Authentication authentication) {

        Member member = (Member) authentication.getPrincipal();
        ReviewWriteResponseDto responseDto = reviewService.editReview(reviewId, reviewWriteRequestDto, member);

        return ResponseEntity.ok(responseDto);
    }
    
    //리뷰 삭제
    @ApiOperation(value = "리뷰 삭제 API", notes = "리뷰 ID 에 해당하는 리뷰를 삭제합니다.(로그인 후 이용가능)")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity deleteReview(
            @ApiParam(value = "리뷰 ID", required = true) @PathVariable Long reviewId,
            Authentication authentication) {

        Member member = (Member) authentication.getPrincipal();
        reviewService.deleteReview(reviewId, member);

        return ResponseEntity.noContent().build();
    }
    
    //리뷰 좋아요
    @ApiOperation(value = "리뷰 좋아요 API", notes = "리뷰 ID 에 해당하는 리뷰를 좋아요를 누릅니다.(로그인 후 이용가능)")
    @PatchMapping("/{reviewId}/like")
    public ResponseEntity likeReview(
            @ApiParam(value = "리뷰 ID", required = true) @PathVariable Long reviewId,
            Authentication authentication) {

        reviewService.likeReview(reviewId, (Member)authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }
    
    //bset 리뷰
    @ApiOperation(value = "베스트 리뷰 조회 API", notes = "좋아요 개수가 1 이상인 리뷰를 좋아요 순으로 조회 및 페이징. 쿼리스트링 예시(?page=0&size=10)")
    @GetMapping("/best")
    public ResponseEntity getBestReviews(Pageable pageable) {
        Page<ReviewRetrieveResponseDto> responseDto = reviewService.getBestReviews(pageable);

        return ResponseEntity.ok(responseDto);
    }

}
