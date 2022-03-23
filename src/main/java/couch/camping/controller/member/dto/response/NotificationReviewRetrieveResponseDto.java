package couch.camping.controller.member.dto.response;

import couch.camping.domain.notification.entity.Notification;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ApiModel(description = "회원의 리뷰 좋아요 알림 조회 응답 DTO")
public class NotificationReviewRetrieveResponseDto extends MemberNotificationResponseDto {

    @ApiModelProperty(required = true, value = "리뷰 ID", example = "24")
    private Long reviewId;
    @ApiModelProperty(required = true, value = "캠핑장 ID", example = "30")
    private Long campId;
    @ApiModelProperty(required = true, value = "리뷰 내용", example = "여기 좋아요")
    private String content;
    @ApiModelProperty(required = true, value = "작성 날짜", example = "2022-02-18T18:26:23.9592352")
    private String imgUrl;
    @ApiModelProperty(required = true, value = "작성 날짜", example = "2022-02-18T18:26:23.9592352")
    private String facltNm;

    public NotificationReviewRetrieveResponseDto(Notification notification) {
        super(notification, "reviewLike");
        this.reviewId = notification.getReview().getId();
        this.campId = notification.getReview().getCamp().getId();
        this.content = notification.getReview().getContent();
        this.imgUrl = notification.getReview().getCamp().getFirstImageUrl();
        this.facltNm = notification.getReview().getCamp().getFacltNm();
    }
}
