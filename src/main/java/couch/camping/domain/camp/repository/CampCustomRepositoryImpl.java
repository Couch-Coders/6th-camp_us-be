package couch.camping.domain.camp.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.controller.camp.dto.response.CampResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.entity.QCamp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class CampCustomRepositoryImpl implements CampCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Camp findByCampId(Long campId) {

        Camp camp = queryFactory
                .selectFrom(QCamp.camp)
                .where(QCamp.camp.id.eq(campId))
                .fetchOne();

        return camp;
    }
}
