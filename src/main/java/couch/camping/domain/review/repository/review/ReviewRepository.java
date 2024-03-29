package couch.camping.domain.review.repository.review;

import couch.camping.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {
}
