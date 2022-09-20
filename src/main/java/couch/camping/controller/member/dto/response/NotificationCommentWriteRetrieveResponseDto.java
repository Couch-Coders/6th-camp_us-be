package couch.camping.controller.member.dto.response;

import couch.camping.domain.notification.entity.Notification;
import couch.camping.domain.post.entity.PostImage;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ApiModel(description = "회원의 게시글에 댓글 달릴 때 알림 조회 응답 DTO")
public class NotificationCommentWriteRetrieveResponseDto extends MemberNotificationResponseDto {

    private Long commentId;

    private Long postId;

    private String postTitle;

    private String content;

    private List<String> postImgUrlList = new ArrayList<>();

    public NotificationCommentWriteRetrieveResponseDto(Notification notification) {
        super(notification, "commentWrite");
        this.commentId = notification.getWriteComment().getId();
        this.content = notification.getWriteComment().getContent();
        this.postTitle = notification.getWriteComment().getPost().getTitle();
        for (PostImage postImage : notification.getWriteComment().getPost().getPostImageList()) {
            this.postImgUrlList.add(postImage.getImgUrl());
        }
    }
}
