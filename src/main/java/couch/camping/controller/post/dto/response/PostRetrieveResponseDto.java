package couch.camping.controller.post.dto.response;

import couch.camping.domain.post.entity.Post;
import couch.camping.domain.postimage.entity.PostImage;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class PostRetrieveResponseDto {

    private Long postId;

    private Long memberId;

    private String content;

    private String postType;

    private int likeCnt;

    private int commentCnt;

    private List<String> imgUrlList = new ArrayList<>();

    private LocalDateTime createdDate;
    
    private String memberImgUrl;

    private String nickname;

    public PostRetrieveResponseDto(Post post, int commentCnt, List<PostImage> postImageList) {
        this.postId = post.getId();
        this.memberId = post.getMember().getId();
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
