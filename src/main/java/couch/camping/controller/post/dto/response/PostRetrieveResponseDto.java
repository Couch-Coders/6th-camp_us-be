package couch.camping.controller.post.dto.response;

import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.entity.PostImage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@ApiModel(description = "커뮤니티 게시글 조회 응답 DTO")
public class PostRetrieveResponseDto {

    @ApiModelProperty(required = true, value = "게시글 ID", example = "2716")
    private Long postId;

    @ApiModelProperty(required = true, value = "회원 ID", example = "7108")
    private Long memberId;

    @ApiModelProperty(required = true, value = "게시글 제목", example = "질문있습니다!")
    private String title;

    @ApiModelProperty(required = true, value = "게시글 내용", example = "안녕하세용")
    private String content;

    @ApiModelProperty(required = true, value = "게시글 종류", example = "free")
    private String postType;

    @ApiModelProperty(required = true, value = "좋아요 개수", example = "10")
    private int likeCnt;

    @ApiModelProperty(required = true, value = "댓글 개수", example = "12")
    private int commentCnt;

    @ApiModelProperty(required = true, value = "게시글 업로드 된 이미지", example = "[\"www.abc.com\", \"www.avb.com\"]")
    private List<String> imgUrlList = new ArrayList<>();

    @ApiModelProperty(required = true, value = "댓글 생성 날짜", example = "2022-03-15 00:05:57")
    private LocalDateTime createdDate;

    @ApiModelProperty(required = true, value = "이미지 URL", example = "http:www.balladang.com")
    private String memberImgUrl;

    @ApiModelProperty(required = true, value = "닉네임", example = "test")
    private String nickname;


    public PostRetrieveResponseDto(Post post, int commentCnt, List<PostImage> postImageList) {
        this.postId = post.getId();
        this.memberId = post.getMember().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.postType = post.getPostType();
        this.likeCnt = post.getLikeCnt();
        this.commentCnt = commentCnt;
        for (PostImage postImage : postImageList) {
            imgUrlList.add(postImage.getImgUrl());
        }
        this.createdDate = post.getCreatedDate();
        this.memberImgUrl = post.getMember().getImgUrl();
        this.nickname = post.getMember().getNickname();
    }
}
