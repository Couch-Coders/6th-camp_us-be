package couch.camping.domain.review.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.List;

import static couch.camping.domain.review.entity.QReview.review;

@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Page<Review> findAllByLikeCntGreaterThan(Pageable pageable) {
        JPAQuery<Review> query = queryFactory
                .selectFrom(review)
                .where(review.likeCnt.goe(1))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.likeCnt.desc());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(review.getType(), review.getMetadata());
            query.orderBy(
                    new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty()))
            );
        }

        List<Review> content = query.fetch();

        Long total = queryFactory
                .select(review.count())
                .from(review)
                .where(review.likeCnt.goe(1))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Review> findByCampId(Pageable pageable, Long campId) {
        JPAQuery<Review> query = queryFactory
                .selectFrom(review)
                .where(review.camp.id.eq(campId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(review.getType(), review.getMetadata());
            query.orderBy(
                    new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty()))
            );
        }

        List<Review> content = query.fetch();

        Long total = queryFactory
                .select(review.count())
                .from(review)
                .where(review.camp.id.eq(campId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Review> findByMemberId(Pageable pageable, Long memberId) {
        JPAQuery<Review> query = queryFactory
                .selectFrom(review)
                .where(review.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(review.getType(), review.getMetadata());
            query.orderBy(
                    new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty()))
            );
        }

        List<Review> content = query.fetch();

        Long total = queryFactory
                .select(review.count())
                .from(review)
                .where(review.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public void deleteByReviewId(Long reviewId) {
        queryFactory
                .delete(review)
                .where(review.id.eq(reviewId))
                .execute();

        em.flush();
        em.clear();
    }
}
