package couch.camping.controller.member.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveRequestDto {

    private String uid;
    private String email;
    private String name;
    private String nickname;
    private String imgUrl;
}
