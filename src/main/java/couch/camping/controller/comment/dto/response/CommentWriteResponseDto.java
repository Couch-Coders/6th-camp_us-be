package couch.camping.controller.comment.dto.response;

import couch.camping.domain.comment.entity.Comment;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ApiModel(description = "커뮤니티 댓글 작성 응답 DTO")
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
