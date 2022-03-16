package couch.camping.domain.comment.entity;

import couch.camping.domain.base.BaseEntity;
import couch.camping.domain.commentlike.entity.CommentLike;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.post.entity.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Comment extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder.Default
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<CommentLike> commentLikeList = new ArrayList<>();

    @Lob
    private String content;

    private int likeCnt;

    public void editComment(String content){
        this.content = content;
    }

    public void increaseLikeCnt() {
        this.likeCnt = likeCnt+1;
    }

    public void decreaseLikeCnt() {
        this.likeCnt = likeCnt - 1;
    }

}
