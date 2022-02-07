package couch.camping.controller.member.dto.response;

import couch.camping.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberRegisterResponseDto {
    private Long memberId;
    private String uid;
    private String email;
    private String name;
    private String nickname;
    private String imgUrl;

    public MemberRegisterResponseDto(Member member) {
        this.memberId = member.getId();
        this.uid = member.getUid();
        this.email = member.getEmail();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.imgUrl = member.getImgUrl();
    }
}
