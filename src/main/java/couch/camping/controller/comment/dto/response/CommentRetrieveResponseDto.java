package couch.camping.controller.comment.dto.response;


import couch.camping.domain.comment.entity.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "로그인 전 댓글 조회 응답 DTO")
public class CommentRetrieveResponseDto {

    @ApiModelProperty(required = true, value = "댓글 ID", example = "2710")
    private Long commentId;

    @ApiModelProperty(required = true, value = "회원 ID", example = "7108")
    private Long memberId;

    @ApiModelProperty(required = true, value = "게시글 ID", example = "2716")
    private Long postId;

    @ApiModelProperty(required = true, value = "게시글 내용", example = "안녕하세용")
    private String content;

    @ApiModelProperty(required = true, value = "좋아요 개수", example = "10")
    private int likeCnt;

    @ApiModelProperty(required = true, value = "댓글 생성 날짜", example = "2022-03-15 00:05:57")
    private LocalDateTime createdDate;

    @ApiModelProperty(required = true, value = "닉네임", example = "test")
    private String nickname;

    @ApiModelProperty(required = true, value = "이미지 URL", example = "http:www.balladang.com")
    private String memberImgUrl;

    public CommentRetrieveResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.likeCnt = comment.getLikeCnt();
        this.postId = comment.getPost().getId();
        this.createdDate = comment.getCreatedDate();
        this.nickname = comment.getMember().getNickname();
        this.memberImgUrl = comment.getMember().getImgUrl();
    }
}
