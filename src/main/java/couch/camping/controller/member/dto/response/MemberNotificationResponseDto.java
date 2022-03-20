package couch.camping.controller.member.dto.response;

import couch.camping.domain.notification.entity.Notification;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@ApiModel(description = "회원의 알림 조회 응답 DTO")
public class MemberNotificationResponseDto {

    @ApiModelProperty(required = true, value = "알림 ID", example = "15")
    private Long notificationId;
    @ApiModelProperty(required = true, value = "알림을 보낸 회원 ID", example = "12")
    private Long memberId;
    @ApiModelProperty(required = true, value = "알림을 보낸 회원 닉네임", example = "abcd")
    private String nickname;
    @ApiModelProperty(required = true, value = "리뷰를 읽었는지", example = "true")
    private boolean isChecked;
    @ApiModelProperty(required = true, value = "작성 날짜", example = "2022-02-18T18:26:23.9592352")
    private LocalDateTime createdDate;
    @ApiModelProperty(required = true, value = "알림 유형", example = "post, comment, review")
    private String notificationType;


    public MemberNotificationResponseDto(Notification notification, String notificationType) {
        this.notificationId = notification.getId();
        this.memberId = notification.getMember().getId();
        this.nickname = notification.getMember().getNickname();
        this.isChecked = notification.isChecked();
        this.createdDate = notification.getCreatedDate();
        this.notificationType = notificationType;
    }
}
