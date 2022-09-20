package couch.camping.domain.camp.repository.camp_like;

import couch.camping.domain.camp.entity.CampLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampLikeRepository extends JpaRepository<CampLike, Long>, CampLikeCustomRepository {

    Optional<CampLike> findByCampIdAndMemberId(Long campId, Long memberId);
}
