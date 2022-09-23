package couch.camping.domain.camp.repository.camp_like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.camp.entity.CampLike;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static couch.camping.domain.camp.entity.QCampLike.campLike;

@RequiredArgsConstructor
public class CampLikeCustomRepositoryImpl implements CampLikeCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CampLike> findAllByMemberId(Long memberId) {
        return queryFactory.selectFrom(campLike)
                .where(campLike.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public long countByMemberId(Long memberId) {
        return queryFactory
                .select(campLike.count())
                .from(campLike)
                .where(campLike.member.id.eq(memberId))
                .fetchOne();
    }
}
