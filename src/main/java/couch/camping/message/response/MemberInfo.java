package couch.camping.message.response;

import couch.camping.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberInfo {
    private String uid;
    private String email;
    private String nickname;

    public MemberInfo(Member member) {
        this.uid = member.getUsername();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
