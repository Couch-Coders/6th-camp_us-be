package couch.camping.controller.review.dto.response;


import couch.camping.domain.review.entity.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRetrieveResponseDto {

    private Long reviewId;
    private Long memberId;
    private Long campId;
    private String imgUrl;
    private String content;
    private int rateCnt;
    private int likeCnt;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public ReviewRetrieveResponseDto(Review r) {
        this.reviewId = r.getId();
        this.memberId = r.getMember().getId();
        this.campId = r.getCamp().getId();
        this.imgUrl= r.getImgUrl();
        this.content = r.getContent();
        this.rateCnt = r.getRate();
        this.likeCnt =r.getLikeCnt();
        this.createdDate = r.getCreatedDate();
        this.lastModifiedDate = r.getLastModifiedDate();
    }
}
