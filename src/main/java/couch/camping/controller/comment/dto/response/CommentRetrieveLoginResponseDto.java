package couch.camping.controller.comment.dto.response;


import couch.camping.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRetrieveLoginResponseDto extends CommentRetrieveResponseDto{

    private boolean isChecked;

    public CommentRetrieveLoginResponseDto(Comment comment, boolean isChecked) {
        super(comment);
        this.isChecked = isChecked;
    }
}
