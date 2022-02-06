package couch.camping.domain.camp.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CampCustomRepositoryImpl implements CampCustomRepository {

    private final JPAQueryFactory queryFactory;

}
