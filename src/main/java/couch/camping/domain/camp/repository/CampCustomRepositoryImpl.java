package couch.camping.domain.camp.repository;

import com.querydsl.core.BooleanBuilder;
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

@RequiredArgsConstructor
@Slf4j
public class CampCustomRepositoryImpl implements CampCustomRepository{

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Page<Camp> findAllCampSearch(List<String> tagList, String name, String doNm, String sigunguNm, String sort, Pageable pageable, Float mapX, Float mapY) {

        if (sort.equals("rate")) {

            BooleanBuilder builder = new BooleanBuilder();

            for (String s : tagList) {
                if (s.equals("애견동반")) {
                    builder.and(camp.animalCmgCl.like("가능%"));
                } else {
                    builder.and(camp.sbrsCl.contains(s));
                }
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

            List<Camp> content = queryFactory
                    .selectFrom(camp)
                    .where(builder)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(camp.avgRate.desc(), camp.id.asc())
                    .fetch();

            long total = queryFactory
                    .select(camp.count())
                    .from(camp)
                    .where(builder)
                    .fetchOne();

            return new PageImpl<>(content, pageable, total);
        } else {

            StringBuilder sb = new StringBuilder();
        
            if (!tagList.isEmpty() || name != null) {//태그가 있거나 이름이 null 아니면 where 을 달아준다.
                sb.append("where ");
            }

            for (int i = 0; i < tagList.size(); i++) {
                if (tagList.get(i).equals("애견동반")) {
                    sb.append("animal_cmg_cl like \"%가능%\" ");
                } else {
                    sb.append("sbrs_cl like \"%" + tagList.get(i) +"%\" ");
                }

                if (i < tagList.size() - 1) {
                    sb.append("and ");
                }
            }

            if (name != null) {
                if (tagList.isEmpty())
                    sb.append("faclt_nm like \"%"+ name +"%\" ");
                else
                    sb.append("and faclt_nm like \"%"+ name +"%\" ");
            }

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
                    "FROM camp " + sb +" ORDER BY distance asc";

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

            return new PageImpl<>(content, pageable, 0);
        }
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
}