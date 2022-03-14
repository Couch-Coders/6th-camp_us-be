package couch.camping.controller.comment.dto.response;


import couch.camping.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRetrieveAllResponseDto {

    private String content;

    private Long commentId;

    private Long memberId;

    private int likeCnt;

    private Long postId;


    public CommentRetrieveAllResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.likeCnt = comment.getLikeCnt();
        this.postId = comment.getPost().getId();
    }
}
