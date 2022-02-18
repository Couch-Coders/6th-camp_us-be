package couch.camping.domain.camp.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static couch.camping.domain.camp.entity.QCamp.camp;

@RequiredArgsConstructor
@Slf4j
public class CampCustomRepositoryImpl implements CampCustomRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Camp> findAllCampSearch(List<String> tagList, String sigunguNm, String sort, Pageable pageable, Float mapX, Float mapY) {

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

        if (sort.equals("rate")) {
            query.orderBy(camp.rate.desc());
        }

        List<Camp> content = query.fetch();

        long total = queryFactory
                .select(camp.count())
                .from(camp)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
