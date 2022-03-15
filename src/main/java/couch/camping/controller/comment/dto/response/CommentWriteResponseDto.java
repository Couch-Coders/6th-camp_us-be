package couch.camping.controller.comment.dto.response;

import couch.camping.domain.comment.entity.Comment;
import couch.camping.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentWriteResponseDto {

    private Long commentId;

    private String content;


    public CommentWriteResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
    }
}
