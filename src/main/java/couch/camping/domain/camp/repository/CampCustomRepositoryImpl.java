package couch.camping.domain.camp.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.controller.camp.dto.response.CampSearchPagingResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.entity.QCamp;
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
    public Page<Camp> findAllCampSearch(List<String> tagList, Pageable pageable) {

        List<Camp> results = queryFactory.selectFrom(camp)
                .where(camp.sbrsCl.contains(tagList.get(0)))
                .orderBy(camp.rate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }
}
