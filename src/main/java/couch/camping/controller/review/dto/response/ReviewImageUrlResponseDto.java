package couch.camping.controller.review.dto.response;

import couch.camping.domain.review.entity.Review;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ApiModel(description = "리뷰 조회 응답 DTO")
public class ReviewImageUrlResponseDto {

    @ApiModelProperty(required = true, value = "리뷰 ID", example = "10")
    private Long reviewId;

    @ApiModelProperty(required = true, value = "리뷰 내용", example = "여기 캠핑장 추천합니다.")
    private String content;

    @ApiModelProperty(required = true, value = "닉네임", example = "10")
    private String nickname;

    @ApiModelProperty(required = true, value = "이미지 url", example = "www.imgurl.com")
    private String imgUrl;

    public ReviewImageUrlResponseDto(Review r) {
        this.reviewId = r.getId();
        this.content = r.getContent();
        this.nickname = r.getMember().getNickname();
        this.imgUrl = r.getImgUrl();
    }
}
