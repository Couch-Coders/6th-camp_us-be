package couch.camping.controller.comment.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class CommentWriteRequestDto {

    private String content;
}
