package couch.camping.domain.member.service;

public class MemberRegister {
    private final String uid;
    private final String name;
    private final String email;
    private final String nickname;
    private final String imgUrl;

    public MemberRegister(String uid, String name, String email, String nickname, String imgUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
