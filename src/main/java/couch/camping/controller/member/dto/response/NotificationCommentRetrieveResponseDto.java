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
@ApiModel(description = "회원의 댓글 좋아요 알림 조회 응답 DTO")
public class NotificationCommentRetrieveResponseDto extends MemberNotificationResponseDto {

    private Long commentId;

    private Long postId;

    private String postTitle;

    private String content;

    private List<String> postImgUrlList = new ArrayList<>();

    public NotificationCommentRetrieveResponseDto(Notification notification) {
        super(notification, "commentLike");
        this.commentId = notification.getComment().getId();
        this.postId = notification.getComment().getPost().getId();
        this.content = notification.getComment().getContent();
        this.postTitle = notification.getComment().getPost().getTitle();

        for (PostImage postImage : notification.getComment().getPost().getPostImageList()) {
            this.postImgUrlList.add(postImage.getImgUrl());
        }
    }
}
