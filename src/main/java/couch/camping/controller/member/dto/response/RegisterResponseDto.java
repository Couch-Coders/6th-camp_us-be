package couch.camping.controller.member.dto.response;

import couch.camping.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterResponseDto {
    private String uid;
    private String email;
    private String nickname;
    private String imgUrl;

    public RegisterResponseDto(Member member) {
        this.uid = member.getUsername();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.imgUrl = member.getImgUrl();
    }
}
