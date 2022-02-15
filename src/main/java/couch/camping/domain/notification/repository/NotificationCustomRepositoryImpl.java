package couch.camping.domain.notification.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static couch.camping.domain.notification.entity.QNotification.notification;

@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository{

    private final JPAQueryFactory queryFactory;

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
        JPAQuery<Notification> query = queryFactory
                .selectFrom(notification)
                .join(notification.member).fetchJoin()
                .join(notification.review).fetchJoin()
                .where(notification.ownerMember.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(notification.getType(), notification.getMetadata());
            query.orderBy(
                    new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty()))
            );
        }

        List<Notification> content = query.fetch();

        Long total = queryFactory
                .select(notification.count())
                .from(notification)
                .where(notification.ownerMember.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
