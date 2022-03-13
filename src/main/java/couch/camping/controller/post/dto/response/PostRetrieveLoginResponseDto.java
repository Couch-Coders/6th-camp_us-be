package couch.camping.controller.post.dto.response;

import couch.camping.domain.post.entity.Post;
import couch.camping.domain.postimage.entity.PostImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class PostRetrieveLoginResponseDto extends PostRetrieveResponseDto {

    private boolean isLiked;

    public PostRetrieveLoginResponseDto(Post findPost, int commentCnt, List<PostImage> postImageList, boolean isLiked) {
        super(findPost, commentCnt, postImageList);
        this.isLiked = isLiked;
    }
}
