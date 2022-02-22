package couch.camping.controller.member.dto.response;

import couch.camping.domain.notification.entity.Notification;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@ApiModel(description = "회원의 알림 조회 응답 DTO")
public class NotificationRetrieveResponseDto {

    @ApiModelProperty(required = true, value = "알림 ID", example = "15")
    private Long notificationId;
    @ApiModelProperty(required = true, value = "리뷰 ID", example = "24")
    private Long reviewId;
    @ApiModelProperty(required = true, value = "회원 ID", example = "12")
    private Long memberId;
    @ApiModelProperty(required = true, value = "캠핑장 ID", example = "30")
    private Long campId;
    @ApiModelProperty(required = true, value = "리뷰 내용", example = "여기 좋아요")
    private String content;
    @ApiModelProperty(required = true, value = "회원 닉네임", example = "abcd")
    private String nickname;
    @ApiModelProperty(required = true, value = "리뷰를 읽었는지", example = "true")
    private boolean isChecked;
    @ApiModelProperty(required = true, value = "작성 날짜", example = "2022-02-18T18:26:23.9592352")
    private String imgUrl;
    @ApiModelProperty(required = true, value = "작성 날짜", example = "2022-02-18T18:26:23.9592352")
    private String facltNm;
    @ApiModelProperty(required = true, value = "작성 날짜", example = "2022-02-18T18:26:23.9592352")
    private LocalDateTime createdDate;

    public NotificationRetrieveResponseDto(Notification notification) {
        this.notificationId = notification.getId();
        this.reviewId = notification.getReview().getId();
        this.memberId = notification.getMember().getId();
        this.campId = notification.getReview().getCamp().getId();
        this.content = notification.getReview().getContent();
        this.nickname = notification.getMember().getNickname();
        this.isChecked = notification.isChecked();
        this.imgUrl = notification.getReview().getCamp().getFirstImageUrl();
        this.facltNm = notification.getReview().getCamp().getFacltNm();
        this.createdDate = notification.getCreatedDate();
    }
}
