package couch.camping.domain.notification.service;

import couch.camping.controller.member.dto.response.*;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.notification.entity.Notification;
import couch.camping.domain.notification.repository.NotificationRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Page<MemberNotificationResponseDto> retrieveNotifications(Long memberId, Pageable pageable) {
        return findNotificationByOwnerMemberId(memberId, pageable)
                .map(notification -> notificationMapToRespDto(notification));
    }

    @Transactional
    public void updateNotification(Member member, Long notificationId) {
        Optional<Notification> notificationByNotificationId = findNotificationByNotificationId(notificationId);
        Notification notification = validateOptionalNotification(notificationByNotificationId);

        if (notification.getOwnerMember() != member) {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 알림이 아닙니다.");
        }

        notification.changeIsCheckedTrue();
    }

    @Transactional
    public void updateNotifications(Long memberId) {
        notificationRepository.changeNotifications(memberId);
    }

    @Transactional
    public void deleteNotification(Long notificationId, Member member) {
        Notification notification = validateOptionalNotification(findNotificationByNotificationId(notificationId));

        if (notification.getOwnerMember() != member)
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "해당 회원의 알림이 아닙니다.");

        deleteNotificationByNotificationId(notificationId);
    }

    @Transactional
    public void deleteAllNotification(Long memberId) {
        deleteAllNotificationByMemberId(memberId);
    }

    private void deleteNotificationByNotificationId(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    private Notification validateOptionalNotification(Optional<Notification> notificationByNotificationId) {
        return notificationByNotificationId
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_NOTIFICATION, "알림 ID 에 맞는 알림이 없습니다.");
                });
    }

    private Optional<Notification> findNotificationByNotificationId(Long notificationId) {
        return notificationRepository.findById(notificationId);
    }

    private MemberNotificationResponseDto notificationMapToRespDto(Notification notification) {
        if (notification.getReview() != null)
            return new NotificationReviewRetrieveResponseDto(notification);
        else if (notification.getComment() != null)
            return new NotificationCommentRetrieveResponseDto(notification);
        else if (notification.getPost() != null)
            return new NotificationPostRetrieveResponseDto(notification);
        else
            return new NotificationCommentWriteRetrieveResponseDto(notification);
    }

    private void deleteAllNotificationByMemberId(Long memberId) {
        notificationRepository.deleteByOwnerMemberId(memberId);
    }

    private Page<Notification> findNotificationByOwnerMemberId(Long memberId, Pageable pageable) {
        return notificationRepository.findByOwnerMemberId(pageable, memberId);
    }

    public long countMemberNotReadNotifications(Long memberId) {
        return notificationRepository.countUnReadMemberNotifications(memberId);
    }
}
