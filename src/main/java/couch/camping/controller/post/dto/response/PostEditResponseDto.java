package couch.camping.controller.post.dto.response;

import couch.camping.domain.post.entity.Post;
import couch.camping.domain.postimage.entity.PostImage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@ApiModel(description = "커뮤니티 게시글 수정 응답 DTO")
@EqualsAndHashCode
public class PostEditResponseDto {

    @ApiModelProperty(required = true, value = "게시글 ID", example = "2821")
    private Long postId;

    @ApiModelProperty(required = true, value = "게시글 제목", example = "질문있습니다!")
    private String title;

    @ApiModelProperty(required = true, value = "게시글 내용", example = "안녕하세용")
    private String content;

    @ApiModelProperty(required = true, value = "게시글 종류", example = "free")
    private String postType;

    @ApiModelProperty(required = true, value = "이미지 URL", example = "http:www.balladang.com")
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
