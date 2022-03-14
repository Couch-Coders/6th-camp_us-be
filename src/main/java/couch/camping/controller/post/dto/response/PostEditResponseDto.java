package couch.camping.controller.post.dto.response;

import couch.camping.domain.post.entity.Post;
import couch.camping.domain.postimage.entity.PostImage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class PostEditResponseDto {

    private Long postId;

    private String content;

    private String postType;

    private List<String> imgUrlList = new ArrayList<>();

    public PostEditResponseDto(Post savePost, List<PostImage> postImageList) {
        this.postId = savePost.getId();
        this.content = savePost.getContent();
        this.postType = savePost.getPostType();
        for (PostImage postImage : postImageList) {
            imgUrlList.add(postImage.getImgUrl());
        }
    }

}
