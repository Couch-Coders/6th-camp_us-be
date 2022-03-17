package couch.camping.controller.post.dto.response;

import couch.camping.domain.post.entity.Post;
import couch.camping.domain.postimage.entity.PostImage;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@ApiModel(description = "커뮤니티 게시글 수정 응답 DTO")
public class PostEditResponseDto {

    private Long postId;

    private String title;

    private String content;

    private String postType;

    private List<String> imgUrlList = new ArrayList<>();

    public PostEditResponseDto(Post post, List<PostImage> postImageList) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.postType = post.getPostType();
        for (PostImage postImage : postImageList) {
            imgUrlList.add(postImage.getImgUrl());
        }
    }

}
