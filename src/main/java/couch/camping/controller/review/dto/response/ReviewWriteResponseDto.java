package couch.camping.controller.review.dto.response;

import couch.camping.domain.review.entity.Review;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewWriteResponseDto {

    private String content;
    private int rate;
    private String imgUrl;

    public ReviewWriteResponseDto(Review review) {
        this.content = review.getContent();
        this.rate = review.getRate();
        this.imgUrl = review.getImgUrl();
    }
}
