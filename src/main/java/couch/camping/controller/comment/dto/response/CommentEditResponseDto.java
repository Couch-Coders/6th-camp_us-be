package couch.camping.controller.comment.dto.response;

import couch.camping.domain.comment.entity.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @NoArgsConstructor
@Setter @AllArgsConstructor
@ApiModel(description = "커뮤니티 댓글 수정 응답 DTO")
public class CommentEditResponseDto {

    @ApiModelProperty(required = true, value = "댓글 ID", example = "2710")
    private Long commentId;

    @ApiModelProperty(required = true, value = "댓글 내용", example = "감사합니다!")
    private String content;

    public CommentEditResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
    }
}
