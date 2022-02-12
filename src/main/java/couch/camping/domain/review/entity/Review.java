package couch.camping.domain.review.entity;

import couch.camping.domain.base.BaseEntity;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.reviewlike.entity.ReviewLike;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
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

    @OneToMany(mappedBy = "review")
    private List<ReviewLike> reviewLikeList = new ArrayList<>();

    private String imgUrl;

    @Lob
    private String content;

    private int rate;

    private int likeCnt;

    public void increaseLikeCnt() {
         this.likeCnt = likeCnt+1;
    }

    public void decreaseLikeCnt() {
        this.likeCnt = likeCnt - 1;
    }

    public Review changeReview(String content, int rate, String imgUrl) {
        this.content = content;
        this.rate = rate;
        this.imgUrl = imgUrl;
        return this;
    }
}
