package couch.camping.domain.post.entity;

import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.domain.base.BaseEntity;
import lombok.*;

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

    @Lob
    private String content;

    private String hashTag;

    private int likeCnt;

    private int commentCnt;

    public Post(PostWriteRequestDto postWriteRequestDto) {
        this.content = postWriteRequestDto.getContent();
        this.hashTag = postWriteRequestDto.getHashTag();
    }
}
