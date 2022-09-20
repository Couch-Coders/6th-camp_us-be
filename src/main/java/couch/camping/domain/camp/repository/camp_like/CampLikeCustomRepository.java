package couch.camping.domain.camp.repository.camp_like;

import couch.camping.domain.camp.entity.CampLike;

import java.util.List;

public interface CampLikeCustomRepository {
    List<CampLike> findAllByMemberId(Long memberId);

    long countByMemberId(Long memberId);
}
