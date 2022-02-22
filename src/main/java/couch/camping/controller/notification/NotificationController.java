package couch.camping.controller.notification;

import couch.camping.domain.member.entity.Member;
import couch.camping.domain.notification.service.NotificationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    //알림 삭제
    @ApiOperation(value = "알림 단건 삭제 API", notes = "알림 단건 삭제 API")
    @DeleteMapping("{notificationId}")
    public ResponseEntity deleteNotification(
            @ApiParam(value = "알림 ID", required = true) @PathVariable Long notificationId,
            Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        notificationService.deleteNotification(notificationId, member);
        return ResponseEntity.noContent().build();
    }

}
