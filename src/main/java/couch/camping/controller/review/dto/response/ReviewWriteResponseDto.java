package couch.camping.controller.review.dto.response;

import couch.camping.domain.review.entity.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewWriteResponseDto {

    private Long reviewId;
    private Long memberId;
    private Long campId;
    private String content;
    private int rate;
    private String imgUrl;
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
