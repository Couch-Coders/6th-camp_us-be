package couch.camping.domain.review.entity;

import couch.camping.domain.base.BaseTimeEntity;
import couch.camping.domain.member.entity.Member;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ReviewLike extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "review_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public ReviewLike(Member member) {
        this.member = member;
    }

    public void addReview(Review review) {
        if (this.review != null) {
            this.review.getReviewLikeList().remove(this);
        }
        this.review = review;
        review.getReviewLikeList().add(this);
    }
}
