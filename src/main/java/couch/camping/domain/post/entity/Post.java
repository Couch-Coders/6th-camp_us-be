package couch.camping.domain.post.entity;

import couch.camping.domain.base.BaseEntity;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.postimage.entity.PostImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Post extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImageList = new ArrayList<>();

    @Lob
    private String content;

    private String postType;

    private int likeCnt;

    private int commentCnt;

    public void editPost(String content, String hashTag) {
        this.content = content;
        this.postType = hashTag;
    }

    public void increaseLikeCnt() {
        this.likeCnt = likeCnt+1;
    }

    public void decreaseLikeCnt() {
        this.likeCnt = likeCnt - 1;
    }
}
