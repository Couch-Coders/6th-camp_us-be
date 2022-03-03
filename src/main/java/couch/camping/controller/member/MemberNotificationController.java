package couch.camping.controller.member;

import couch.camping.controller.member.dto.response.NotificationRetrieveResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.notification.service.NotificationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberNotificationController {

    private final NotificationService notificationService;

    //회원 알림 조회
    @ApiOperation(value = "회원 알림 조회 API", notes = "Header 의 토큰에 해당하는 회원의 전체 알림을 조회합니다.")
    @GetMapping("/me/notifications")
    public ResponseEntity<Page<NotificationRetrieveResponseDto>> getMemberNotifications(Pageable pageable,
                                                                                        Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(notificationService
                .retrieveNotifications(memberId, pageable));
    }

    //알림 단건 읽음
    @ApiOperation(value = "회원 알림 조회 API", notes = "Header 의 토큰에 해당하는 회원의 전체 알림을 조회합니다.")
    @PatchMapping("/me/notifications/{notificationId}")
    public ResponseEntity updateMemberNotification(@ApiParam(value = "알림 ID", required = true) @PathVariable Long notificationId,
                                                   Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        notificationService.updateNotification(member, notificationId);

        return ResponseEntity.noContent().build();
    }

    //회원의 안읽은 알림 개수
    @ApiOperation(value = "회원의 안읽은 알림 개수 API", notes = "Header 의 토큰에 해당하는 회원의 읽지 않은 알림을 조회합니다.")
    @GetMapping("/me/notifications/count")
    public ResponseEntity countMemberNotReadNotification(Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(notificationService.countMemberNotReadNotifications(memberId));


    }

    //알림 전체 읽음
    @ApiOperation(value = "회원 알림 전체 갱신 API", notes = "Header 의 토큰에 해당하는 회원의 전체 알림을 읽음으로 갱신합니다.")
    @PatchMapping("/me/notifications")
    public ResponseEntity updateMemberNotifications(Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();
        notificationService.updateNotifications(memberId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "회원의 모든 알림 삭제 API", notes = "회원의 모든 알림을 삭제합니다.")
    @DeleteMapping("/me/notifications")
    public ResponseEntity deleteAllNotification(Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();
        notificationService.deleteAllNotification(memberId);
        return ResponseEntity.noContent().build();
    }
}
