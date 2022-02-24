package couch.camping.domain.review.repository;

import couch.camping.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewCustomRepository {

    Page<Review> findAllByLikeCntGreaterThan(Pageable pageable);

    Page<Review> findByCampId(Pageable pageable, Long campId);

    Page<Review> findByMemberId(Pageable pageable, Long memberId);

    Long countByMemberId(Long memberId);

    List<Review> findImageUrlByCampId(Long campId);
}
