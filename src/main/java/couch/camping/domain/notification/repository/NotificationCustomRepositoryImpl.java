package couch.camping.domain.notification.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static couch.camping.domain.notification.entity.QNotification.notification;

@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Optional<Notification> findByMemberIdAndReviewId(Long memberId, Long reviewId) {
        Notification content = queryFactory
                .selectFrom(notification)
                .where(notification.member.id.eq(memberId), notification.review.id.eq(reviewId))
                .fetchOne();

        return Optional.ofNullable(content);
    }

    @Override
    public Page<Notification> findByOwnerMemberId(Pageable pageable, Long memberId) {

        List<Notification> content = queryFactory
                .selectFrom(notification)
                .join(notification.member).fetchJoin()
                .join(notification.review).fetchJoin()
                .where(notification.ownerMember.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notification.createdDate.desc()).fetch();

        Long total = queryFactory
                .select(notification.count())
                .from(notification)
                .where(notification.ownerMember.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public void changeNotifications(Long ownerMemberId) {
        queryFactory
                .update(notification)
                .set(notification.isChecked, true)
                .where(notification.ownerMember.id.eq(ownerMemberId))
                .execute();

        em.flush();
        em.clear();
    }
}
