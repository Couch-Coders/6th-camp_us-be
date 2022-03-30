package couch.camping.controller.comment.dto.response;

import couch.camping.domain.comment.entity.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode
@ApiModel(description = "커뮤니티 댓글 작성 응답 DTO")
public class CommentWriteResponseDto {

    @ApiModelProperty(required = true, value = "댓글 ID", example = "2821")
    private Long commentId;

    @ApiModelProperty(required = true, value = "회원 ID", example = "1204")
    private Long memberId;

    @ApiModelProperty(required = true, value = "게시글 ID", example = "1257")
    private Long postId;

    @ApiModelProperty(required = true, value = "게시글 내용", example = "안녕하세용")
    private String content;

    @ApiModelProperty(required = true, value = "댓글 생성 날짜", example = "2022-03-15 00:05:57")
    private LocalDateTime createdDate;

    public CommentWriteResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.createdDate = comment.getCreatedDate();
    }
}
