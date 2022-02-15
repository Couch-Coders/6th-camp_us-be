package couch.camping.domain.notification.repository;

import couch.camping.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationCustomRepository {

}
