package couch.camping.domain.camp.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static couch.camping.domain.camp.entity.QCamp.camp;
import static couch.camping.domain.review.entity.QReview.review;

@RequiredArgsConstructor
public class CampCustomRepositoryImpl implements CampCustomRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Camp> findAllCampSearch(List<String> tagList, String sigunguNm, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        for (String s : tagList) {
            builder.and(camp.sbrsCl.contains(s));
        }

        if (sigunguNm != null){
            builder.and(camp.sigunguNm.eq(sigunguNm));
        }

        JPAQuery<Camp> query = queryFactory
                .selectFrom(camp)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(camp.getType(), camp.getMetadata());
            query.orderBy(
                    new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty()))
            );
        }

        List<Camp> content = query.fetch();

        long fetchCount = queryFactory.selectFrom(camp)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(content, pageable, fetchCount);
    }
}
