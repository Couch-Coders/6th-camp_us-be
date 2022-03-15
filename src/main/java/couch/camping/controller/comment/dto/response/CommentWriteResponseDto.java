package couch.camping.controller.comment.dto.response;

import couch.camping.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentWriteResponseDto {

    private Long commentId;

    private Long memberId;

    private Long postId;

    private String content;

    private LocalDateTime createdDate;

    public CommentWriteResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.createdDate = comment.getCreatedDate();
    }
}
