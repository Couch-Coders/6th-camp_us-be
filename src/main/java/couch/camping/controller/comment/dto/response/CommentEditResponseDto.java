package couch.camping.controller.comment.dto.response;

import couch.camping.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @NoArgsConstructor
@Setter @AllArgsConstructor
public class CommentEditResponseDto {

    private Long commentId;

    private String content;

    public CommentEditResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
    }
}
