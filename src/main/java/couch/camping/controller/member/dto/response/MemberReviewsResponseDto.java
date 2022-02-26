package couch.camping.controller.member.dto.response;

import couch.camping.domain.review.entity.Review;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "회원이 작성한 리뷰 조회 응답 DTO")
public class MemberReviewsResponseDto {

    @ApiModelProperty(required = true, value = "리뷰 ID", example = "2")
    private Long reviewId;
    @ApiModelProperty(required = true, value = "회원 ID", example = "2")
    private Long memberId;
    @ApiModelProperty(required = true, value = "캠핑장 ID", example = "2")
    private Long campId;
    @ApiModelProperty(required = true, value = "리뷰 이미지 url", example = "www.img.com")
    private String imgUrl;
    @ApiModelProperty(required = true, value = "리뷰 내용", example = "여기 좋아요`")
    private String content;
    @ApiModelProperty(required = true, value = "평점", example = "4")
    private int rate;
    @ApiModelProperty(required = true, value = "리뷰 좋아요 수", example = "2")
    private int likeCnt;
    @ApiModelProperty(required = true, value = "캠핑장 이름", example = "달빛캠핑장")
    private String facltNm;
    @ApiModelProperty(required = true, value = "작성 날짜", example = "2022-02-18T18:26:23.9592352")
    private LocalDateTime createdDate;
    @ApiModelProperty(required = true, value = "수정 날짜", example = "2022-02-19T18:26:23.9592352")
    private LocalDateTime lastModifiedDate;

    public MemberReviewsResponseDto(Review review) {
        this.reviewId = review.getId();
        this.memberId = review.getMember().getId();
        this.campId = review.getCamp().getId();
        this.imgUrl = review.getImgUrl();
        this.content = review.getContent();
        this.rate = review.getRate();
        this.likeCnt = review.getLikeCnt();
        this.facltNm = review.getCamp().getFacltNm();
        this.createdDate = review.getCreatedDate();
        this.lastModifiedDate = review.getLastModifiedDate();
    }
}

