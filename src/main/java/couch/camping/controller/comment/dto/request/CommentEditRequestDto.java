package couch.camping.controller.comment.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentEditRequestDto {

    @ApiModelProperty(required = true, value = "댓글 내용", example = "안녕하세요. 궁금한 게 있어서 질문드립니다. 쏼라쏼라~~")
    private String content;
}
