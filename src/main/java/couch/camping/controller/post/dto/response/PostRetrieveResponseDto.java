package couch.camping.controller.post.dto.response;

import couch.camping.domain.postimage.entity.PostImage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class PostRetrieveResponseDto {

    private Long postId;

    private Long memberId;

    private String content;

    private String hashTag;

    private int likeCnt;

    private int commentCnt;

    private List<String> imgUrlList = new ArrayList<>();


    public PostRetrieveResponseDto(Long postId, Long memberId, String content, String hashTag, int likeCnt, int commentCnt, List<PostImage> postImageList) {
        this.postId = postId;
        this.memberId = memberId;
        this.content = content;
        this.hashTag = hashTag;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
        for (PostImage postImage : postImageList) {
            imgUrlList.add(postImage.getImgUrl());
        }
    }
}
