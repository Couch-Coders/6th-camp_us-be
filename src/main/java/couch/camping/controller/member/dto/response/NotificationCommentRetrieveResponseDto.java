package couch.camping.controller.member.dto.response;

import couch.camping.domain.notification.entity.Notification;
import io.swagger.annotations.ApiModel;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ApiModel(description = "회원의 댓글 좋아요 알림 조회 응답 DTO")
public class NotificationCommentRetrieveResponseDto extends MemberNotificationResponseDto {

    private Long commentId;

    private String content;

    public NotificationCommentRetrieveResponseDto(Notification notification) {
        super(notification, "commentLike");
        this.commentId = notification.getComment().getId();
        this.content = notification.getComment().getContent();
    }
}
