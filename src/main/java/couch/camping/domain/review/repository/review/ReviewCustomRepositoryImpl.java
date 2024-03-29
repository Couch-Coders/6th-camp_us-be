package couch.camping.domain.review.repository.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static couch.camping.domain.camp.entity.QCamp.camp;
import static couch.camping.domain.member.entity.QMember.member;
import static couch.camping.domain.review.entity.QReview.review;

@Slf4j
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> findAllGoeOneLikeCntOrderByLikeCnt(Pageable pageable) {

        List<Review> content = queryFactory
                .selectFrom(review)
                .join(review.camp, camp).fetchJoin()
                .join(review.member, member).fetchJoin()
                .where(review.likeCnt.goe(1))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.likeCnt.desc())
                .fetch();

        Long total = queryFactory
                .select(review.count())
                .from(review)
                .where(review.likeCnt.goe(1))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Review> findByCampId(Pageable pageable, Long campId) {

        List<Review> content = queryFactory
                .selectFrom(review)
                .join(review.camp, camp).fetchJoin()
                .join(review.member, member).fetchJoin()
                .where(review.camp.id.eq(campId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.createdDate.desc())
                .fetch();

        Long total = queryFactory
                .select(review.count())
                .from(review)
                .where(review.camp.id.eq(campId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Review> findByMemberIdWithPaging(Pageable pageable, Long memberId) {

        List<Review> content = queryFactory
                .selectFrom(review)
                .join(review.member, member).fetchJoin()
                .join(review.camp, camp).fetchJoin()
                .where(review.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.createdDate.desc())
                .fetch();

        Long total = queryFactory
                .select(review.count())
                .from(review)
                .where(review.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Long countByMemberId(Long memberId) {
        return queryFactory
                .select(review.count())
                .from(review)
                .where(review.member.id.eq(memberId))
                .fetchOne();
    }

    @Override
    public List<Review> findNotNullImgUrlByCampId(Long campId) {
        List<Review> content = queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .where(review.imgUrl.length().goe(1), review.camp.id.eq(campId))
                .orderBy(review.createdDate.desc())
                .fetch();

        return content;
    }
}
