package couch.camping.controller.comment.dto.response;


import couch.camping.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRetrieveResponseDto {

    private Long commentId;

    private Long memberId;

    private Long postId;

    private String content;

    private int likeCnt;

    private LocalDateTime createdDate;

    public CommentRetrieveResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.likeCnt = comment.getLikeCnt();
        this.postId = comment.getPost().getId();
        this.createdDate = comment.getCreatedDate();
    }
}
