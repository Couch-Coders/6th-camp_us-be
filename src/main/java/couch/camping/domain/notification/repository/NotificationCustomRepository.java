package couch.camping.domain.notification.repository;

import couch.camping.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationCustomRepository {

    Optional<Notification> findByMemberIdAndReviewId(Long memberId, Long reviewId);

    Page<Notification> findByOwnerMemberId(Pageable pageable, Long memberId);
}
