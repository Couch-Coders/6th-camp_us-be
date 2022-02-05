package couch.camping.domain.member.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequestDto {

    private String username;
    private String email;
    private String nickname;
}
