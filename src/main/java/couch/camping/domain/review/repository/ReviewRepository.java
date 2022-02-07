package couch.camping.domain.review.repository;

import couch.camping.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCampId(Long campId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteById(Long reviewId);
}
