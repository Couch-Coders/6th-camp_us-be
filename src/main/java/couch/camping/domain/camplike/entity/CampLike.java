package couch.camping.domain.camplike.entity;

import couch.camping.domain.base.BaseTimeEntity;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.member.entity.Member;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CampLike extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "camp_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
