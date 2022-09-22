package couch.camping.controller.post.dto.response;

import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.entity.PostImage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@ApiModel(description = "로그인 후 커뮤니티 게시글 조회 응답 DTO")
@ToString
public class PostRetrieveLoginResponseDto extends PostRetrieveResponseDto {

    @ApiModelProperty(required = true, value = "로그인 여부", example = "true")
    private boolean isLiked;

    public PostRetrieveLoginResponseDto(Post findPost, int commentCnt, List<PostImage> postImageList, boolean isLiked) {
        super(findPost, commentCnt, postImageList);
        this.isLiked = isLiked;
    }
}
