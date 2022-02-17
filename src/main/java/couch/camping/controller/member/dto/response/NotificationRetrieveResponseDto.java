package couch.camping.controller.member.dto.response;

import couch.camping.domain.notification.entity.Notification;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class NotificationRetrieveResponseDto {

    private Long notificationId;

    private Long reviewId;

    private Long memberId;

    private String content;

    private String nickname;

    private boolean isChecked;

    public NotificationRetrieveResponseDto(Notification notification) {
        this.notificationId = notification.getId();
        this.reviewId = notification.getReview().getId();
        this.memberId = notification.getMember().getId();
        this.content = notification.getReview().getContent();
        this.nickname = notification.getMember().getNickname();
        this.isChecked = notification.isChecked();
    }
}
