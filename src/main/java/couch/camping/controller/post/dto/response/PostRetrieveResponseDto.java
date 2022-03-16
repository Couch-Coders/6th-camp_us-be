package couch.camping.controller.post.dto.response;

import couch.camping.domain.post.entity.Post;
import couch.camping.domain.postimage.entity.PostImage;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@ApiModel(description = "커뮤니티 게시글 조회 응답 DTO")
public class PostRetrieveResponseDto {

    private Long postId;

    private Long memberId;

    private String content;

    private String postType;

    private int likeCnt;

    private int commentCnt;

    private List<String> imgUrlList = new ArrayList<>();

    private LocalDateTime createdDate;

    public PostRetrieveResponseDto(Post findPost, int commentCnt, List<PostImage> postImageList) {
        this.postId = findPost.getId();
        this.memberId = findPost.getMember().getId();
        this.content = findPost.getContent();
        this.postType = findPost.getPostType();
        this.likeCnt = findPost.getLikeCnt();
        this.commentCnt = commentCnt;
        for (PostImage postImage : postImageList) {
            imgUrlList.add(postImage.getImgUrl());
        }
        this.createdDate = findPost.getCreatedDate();
    }
}
