package couch.camping.domain.post.entity;

import couch.camping.domain.base.BaseEntity;
import couch.camping.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PostImage extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String imgUrl;

    public PostImage(Member member, Post post, String imgUrl) {
        this.member = member;
        this.post = post;
        this.imgUrl = imgUrl;
    }
}
