package couch.camping.domain.post.entity;

import couch.camping.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Post extends BaseEntity {

    @Id @Generated
    @Column(name = "post_id")
    private Long id;

    @Lob
    private String content;

    private String hashTag;

    private int likeCnt;

    private int commentCnt;
}
