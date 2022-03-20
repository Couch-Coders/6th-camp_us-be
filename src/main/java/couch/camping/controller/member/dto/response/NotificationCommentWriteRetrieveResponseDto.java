package couch.camping.controller.member.dto.response;

import couch.camping.domain.notification.entity.Notification;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ApiModel(description = "회원의 게시글에 댓글 달릴 때 알림 조회 응답 DTO")
public class NotificationCommentWriteRetrieveResponseDto extends MemberNotificationResponseDto {

    private Long commentId;

    private String content;

    public NotificationCommentWriteRetrieveResponseDto(Notification notification) {
        super(notification, "commentWrite");
        this.commentId = notification.getWriteComment().getId();
        this.content = notification.getWriteComment().getContent();
    }
}
