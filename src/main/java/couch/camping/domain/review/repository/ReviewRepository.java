package couch.camping.domain.review.repository;

import couch.camping.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByLikeCntGreaterThan(Pageable pageable, int cnt);

    Page<Review> findByCampId(Pageable pageable, Long campId);

    Page<Review> findByMemberId(Pageable pageable, Long memberId);

    @Modifying(clearAutomatically = true)
    void deleteById(Long reviewId);
}
