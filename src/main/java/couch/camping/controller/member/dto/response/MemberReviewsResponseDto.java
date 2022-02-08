package couch.camping.controller.member.dto.response;

import couch.camping.domain.review.entity.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberReviewsResponseDto {

    private Long reviewId;
    private Long memberId;
    private Long campId;
    private String imgUrl;
    private String content;
    private int rate;
    private int likeCnt;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public MemberReviewsResponseDto(Review review) {
        this.reviewId = review.getId();
        this.memberId = review.getMember().getId();
        this.campId = review.getCamp().getId();
        this.imgUrl = review.getImgUrl();
        this.content = review.getContent();
        this.rate = review.getRate();
        this.likeCnt = review.getLikeCnt();
        this.createdDate = review.getCreatedDate();
        this.lastModifiedDate = review.getLastModifiedDate();
    }
}

