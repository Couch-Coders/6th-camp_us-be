package couch.camping.controller.member.dto.response;

import couch.camping.domain.post.entity.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberPostResponseDto {

    @ApiModelProperty(required = true, value = "회원 ID", example = "1204")
    private Long memberId;

    @ApiModelProperty(required = true, value = "게시글 ID", example = "1257")
    private Long postId;

    @ApiModelProperty(required = true, value = "게시글 제목", example = "미세먼지 팁 드립니다.")
    private String title;

    @ApiModelProperty(required = true, value = "게시글 내용", example = "안녕하세용")
    private String content;

    @ApiModelProperty(required = true, value = "게시글 업로드 된 이미지", example = "[\"www.abc.com\", \"www.avb.com\"]")
    private List<String> imgUrlList = new ArrayList<>();

    @ApiModelProperty(required = true, value = "게시글 타입", example = "question")
    private String postType;

    @ApiModelProperty(required = true, value = "게시글 좋아요 개수", example = "10")
    private int likeCnt;

    @ApiModelProperty(required = true, value = "게시글의 댓글 개수", example = "9")
    private int commentCnt;

    @ApiModelProperty(required = true, value = "댓글 생성 날짜", example = "2022-03-15 00:05:57")
    private LocalDateTime createdDate;

    public MemberPostResponseDto(Post post) {
        this.memberId = post.getMember().getId();
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.postType = post.getPostType();
        this.likeCnt = post.getLikeCnt();
        this.commentCnt = post.getCommentCnt();
        imgUrlList.addAll(imgUrlList);
        this.createdDate = post.getCreatedDate();
    }
}
