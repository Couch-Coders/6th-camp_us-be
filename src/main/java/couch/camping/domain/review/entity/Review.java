package couch.camping.domain.review.entity;

import couch.camping.domain.base.BaseEntity;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.member.entity.Member;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Review extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    private String address;

    private String detailAddress;

    private String imageFiles;

    @Lob
    private String content;

    private Integer rate;

    @Column(name = "like_cnt")
    private Integer like;
}
