package couch.camping.domain.camp.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.camp.entity.Camp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static couch.camping.domain.camp.entity.QCamp.camp;
import static couch.camping.domain.camplike.entity.QCampLike.campLike;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Slf4j
public class CampCustomRepositoryImpl implements CampCustomRepository{

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanBuilder tagContain(List<String> tagList) {
        BooleanBuilder builder = new BooleanBuilder();

        for (String s : tagList) {
            if (s.equals("애견동반")) {
                builder.and(camp.animalCmgCl.like("가능%"));
            } else {
                builder.and(camp.sbrsCl.contains(s));
            }
        }
        return builder;
    }
    private BooleanExpression nameContain(String name) {
        return hasText(name) ? camp.facltNm.contains(name) : null;
    }
    private BooleanExpression sigunguNmContain(String sigunguNm) {
        return hasText(sigunguNm) ? camp.sigunguNm.contains(sigunguNm) : null;
    }
    private BooleanExpression doNmContain(String doNm) {
        return hasText(doNm) ? camp.doNm.contains(doNm) : null;
    }

    private StringBuilder getStringBuilder(List<String> tagList, String name) {
        StringBuilder sb = new StringBuilder();

        if (!tagList.isEmpty() || hasText(name)) {//태그가 있거나 이름이 null 아니면 where 을 달아준다.
            sb.append("where ");
        }

        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).equals("애견동반")) {
                sb.append("animal_cmg_cl like \"가능%\" ");
            } else {
                sb.append("sbrs_cl like \"%" + tagList.get(i) +"%\" ");
            }

            if (i < tagList.size() - 1) {
                sb.append("and ");
            }
        }

        if (hasText(name)) {
            if (tagList.isEmpty())
                sb.append("faclt_nm like \"%"+ name +"%\" ");
            else
                sb.append("and faclt_nm like \"%"+ name +"%\" ");
        }
        return sb;
    }

    @Override
    public Page<Camp> findAllCampBySearchCondOrderByRate(List<String> tagList, String name, String doNm, String sigunguNm, int rate, Pageable pageable) {

            List<Camp> content = queryFactory
                    .selectFrom(camp)
                    .where(tagContain(tagList), nameContain(name), doNmContain(doNm), sigunguNmContain(sigunguNm), camp.avgRate.goe(rate))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(camp.avgRate.desc(), camp.id.asc())
                    .fetch();

            long total = queryFactory
                    .select(camp.count())
                    .from(camp)
                    .where(tagContain(tagList), nameContain(name), doNmContain(doNm), sigunguNmContain(sigunguNm), camp.avgRate.goe(rate))
                    .fetchOne();

            return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Camp> findAllCampBySearchCondOrderByDistanceNativeQuery(List<String> tagList, String name, Pageable pageable, Float mapX, Float mapY) {
        String condition = getStringBuilder(tagList, name).toString();

        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();
        String sql = "SELECT *,\n" +
                "    (\n" +
                "      6371 * acos (\n" +
                "      cos ( radians(camp.mapy) )\n" +
                "      * cos( radians( :mapY ) )\n" +
                "      * cos( radians( :mapX ) - radians(camp.mapx) )\n" +
                "      + sin ( radians(camp.mapy) )\n" +
                "      * sin( radians( :mapY ) )\n" +
                "    )\n" +
                ") AS distance\n" +
                "FROM camp " + condition + " ORDER BY distance asc";

        Long total = queryFactory
                .select(camp.count())
                .from(camp)
                .where(tagContain(tagList), nameContain(name))
                .fetchOne();

        Query nativeQuery = em.createNativeQuery(sql);
        nativeQuery.setParameter("mapX", mapX);
        nativeQuery.setParameter("mapY", mapY);
        nativeQuery.setFirstResult(offset);
        nativeQuery.setMaxResults(pageSize);

        List<Object[]> resultList = nativeQuery.getResultList();
        List<Camp> content = new ArrayList<>();
        for (Object[] o : resultList) {
            content.add(new Camp(o));
        }

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Camp> findMemberLikeCamp(Long memberId, Pageable pageable) {

        JPAQuery<Camp> query = queryFactory
                .select(campLike.camp)
                .from(campLike)
                .where(campLike.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(campLike.createdDate.desc());

        List<Camp> content = query.fetch();

        Long total = queryFactory
                .select(campLike.camp.count())
                .from(campLike)
                .where(campLike.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Camp> findAllByCampId(List<Long> campIds) {
        return queryFactory
                .select(camp)
                .from(camp)
                .where(camp.id.in(campIds))
                .fetch();
    }
}