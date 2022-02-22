package couch.camping.domain.notification.repository;

import couch.camping.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationCustomRepository {

    @Modifying(clearAutomatically = true)
    @Query("delete from Notification n where n.ownerMember.id = :memberId")
    void deleteByOwnerMemberId(@Param("memberId") Long memberId);
}
