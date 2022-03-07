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
public class Post extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Lob
    private String content;

    private String hashTag;

    private int likeCnt;

    private int commentCnt;

    public void editPost(String content, String hashTag) {
        this.content = content;
        this.hashTag = hashTag;
    }

}
