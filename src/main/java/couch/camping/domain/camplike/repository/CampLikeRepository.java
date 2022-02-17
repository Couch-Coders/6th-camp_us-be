package couch.camping.domain.camplike.repository;

import couch.camping.domain.camplike.entity.CampLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampLikeRepository extends JpaRepository<CampLike, Long> {

    Optional<CampLike> findByCampIdAndMemberId(Long campId, Long memberId);
}
