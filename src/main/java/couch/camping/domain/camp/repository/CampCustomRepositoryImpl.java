package couch.camping.domain.camp.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.camp.entity.Camp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static couch.camping.domain.camp.entity.QCamp.camp;

@RequiredArgsConstructor
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
