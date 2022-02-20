package couch.camping.domain.camplike.repository;

import couch.camping.domain.camplike.entity.CampLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampLikeRepository extends JpaRepository<CampLike, Long>, CampLikeCustomRepository {

    Optional<CampLike> findByCampIdAndMemberId(Long campId, Long memberId);
}
