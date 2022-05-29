package couch.camping.domain.camplike.repository;

import couch.camping.domain.camplike.entity.CampLike;

import java.util.List;

public interface CampLikeCustomRepository {
    List<CampLike> findAllByMemberId(Long memberId);

    long countByMemberId(Long memberId);
}
