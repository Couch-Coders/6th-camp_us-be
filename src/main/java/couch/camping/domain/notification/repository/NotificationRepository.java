package couch.camping.domain.notification.repository;

import couch.camping.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByMemberIdAndReviewId(Long memberId, Long reviewId);
}
