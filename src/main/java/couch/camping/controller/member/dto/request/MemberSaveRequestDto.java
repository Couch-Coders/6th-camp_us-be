package couch.camping.controller.member.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveRequestDto {

    private String username;
    private String email;
    private String nickname;
}
