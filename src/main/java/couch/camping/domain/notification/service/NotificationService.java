package couch.camping.domain.notification.service;

import couch.camping.domain.notification.entity.Notification;
import couch.camping.domain.notification.repository.NotificationRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void updateNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_NOTIFICATION, "알림 ID 에 맞는 알림이 없습니다.");
                });

        notification.changeIsChecked();
    }

}
