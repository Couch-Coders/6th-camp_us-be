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
    public Page<Camp> findAllCampSearch(List<String> tagList, String name, String doNm, String sigunguNm, String sort, Pageable pageable, Float mapX, Float mapY) {

        BooleanBuilder builder = new BooleanBuilder();

        for (String s : tagList) {
            if (s.equals("애견동반")) {
                builder.and(camp.animalCmgCl.like("가능%"));
                continue;
            }
            builder.and(camp.sbrsCl.contains(s));
        }

        if (name != null) {
            builder.and(camp.facltNm.contains(name));
        }

        if (sigunguNm != null){
            builder.and(camp.sigunguNm.contains(sigunguNm));
        }

        if (doNm != null){
            builder.and(camp.addr1.contains(doNm));
        }

        JPAQuery<Camp> query = queryFactory
                .selectFrom(camp)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        long total = queryFactory
                .select(camp.count())
                .from(camp)
                .where(builder)
                .fetchOne();

        List<Camp> content = query.fetch();

        if (sort.equals("rate")) {
            query.orderBy(camp.avgRate.desc());
        }
//        } else {
//            List<Camp> content = query.fetch();
//
//            Collections.sort(content, (o1, o2) -> {
//                double dis1 = Math.pow(o1.getMapX() - mapX, 2) + Math.pow(o1.getMapY() - mapY, 2);
//                double sqrt1 = Math.sqrt(dis1);
//
//                double dis2 = Math.pow(o2.getMapX() - mapX, 2) + Math.pow(o2.getMapY() - mapY, 2);
//                double sqrt2 = Math.sqrt(dis2);
//
//                if (sqrt1 > sqrt2) {
//                    return 1;//
//                } else {
//                    return -1;
//                }
//            });
//
//            return new PageImpl<>(content, pageable, total);
//        }
        return new PageImpl<>(content, pageable, total);

    }
}
