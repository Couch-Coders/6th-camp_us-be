package couch.camping.controller.review.dto.response;

import couch.camping.domain.review.entity.Review;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "리뷰 작성 응답 DTO")
public class ReviewWriteResponseDto {

    @ApiModelProperty(required = true, value = "리뷰 ID", example = "10")
    private Long reviewId;
    @ApiModelProperty(required = true, value = "회원 ID", example = "3")
    private Long memberId;
    @ApiModelProperty(required = true, value = "캠핑장 ID", example = "1")
    private Long campId;
    @ApiModelProperty(required = true, value = "리뷰 내용", example = "여기 캠핑장 추천합니다.")
    private String content;
    @ApiModelProperty(required = true, value = "리뷰 내용", example = "1~5 사이의 자연수")
    private int rate;
    @ApiModelProperty(required = true, value = "리뷰 이미지 url", example = "https://gocamping.or.kr/upload/camp/1023/thumb/thumb_720_8920mL8X4QwnXtiSF10BNgEW.jpg")
    private String imgUrl;
    @ApiModelProperty(required = true, value = "작성 날짜", example = "2022-02-18T18:26:23.9592352")
    private LocalDateTime createdDate;

    public ReviewWriteResponseDto(Review review) {
        this.reviewId = review.getId();
        this.memberId = review.getMember().getId();
        this.campId = review.getCamp().getId();
        this.content = review.getContent();
        this.rate = review.getRate();
        this.imgUrl = review.getImgUrl();
        this.createdDate = review.getCreatedDate();
    }
}
