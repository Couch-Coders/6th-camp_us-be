package couch.camping.domain.reviewlike.repository;

import couch.camping.domain.reviewlike.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByReviewIdAndMemberId(Long reviewId, Long memberId);
}

