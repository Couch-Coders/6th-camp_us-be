package couch.camping.controller.review.dto.response;


import couch.camping.domain.review.entity.Review;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "로그인 후 리뷰 조회 응답 DTO")
public class ReviewRetrieveLoginResponse extends ReviewRetrieveResponseDto{

    @ApiModelProperty(required = true, value = "좋아요를 눌렀으면 true", example = "true")
    private boolean isLiked;

    public ReviewRetrieveLoginResponse(Review review, boolean isLiked) {
        super(review);
        this.isLiked = isLiked;
    }
}
