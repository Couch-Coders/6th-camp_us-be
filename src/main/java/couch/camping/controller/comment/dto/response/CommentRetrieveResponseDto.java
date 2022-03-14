package couch.camping.controller.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRetrieveResponseDto {

    private String content;

    private Long commentId;

    private Long memberId;

    private int likeCnt;
}
