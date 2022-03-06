package couch.camping.domain.comment.entity;

import couch.camping.domain.base.BaseEntity;
import couch.camping.domain.post.entity.Post;
import lombok.*;

import javax.persistence.*;

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
    @JoinColumn(name = "post_id")
    private Post post;

    @Lob
    private String content;

    private int likeCnt;

}
