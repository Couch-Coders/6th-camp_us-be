package couch.camping.controller.member.dto.response;

import couch.camping.domain.member.entity.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "회원 마이페이지 조회 응답 DTO")
public class MemberRetrieveResponseDto {

    @ApiModelProperty(required = true, value = "회원 ID", example = "2")
    private Long memberId;
    @ApiModelProperty(required = true, value = "구글 UID", example = "asjdhaskudhasiudgflksd23")
    private String uid;
    @ApiModelProperty(required = true, value = "회원 이메일", example = "abcd@gmail.com")
    private String email;
    @ApiModelProperty(required = true, value = "회원 이름", example = "김상운")
    private String name;
    @ApiModelProperty(required = true, value = "회원 닉네임", example = "abcd")
    private String nickname;
    @ApiModelProperty(required = true, value = "회원 이미지 url", example = "www.img.com")
    private String imgUrl;
    @ApiModelProperty(required = true, value = "가입 날짜", example = "2022-02-18T18:26:23.9592352")
    private LocalDateTime createdDate;
    @ApiModelProperty(required = true, value = "회원이 작성한 리뷰 개수", example = "4")
    private long reviewCnt;

    public MemberRetrieveResponseDto(Member member, long reviewCnt) {
        this.memberId = member.getId();
        this.uid = member.getUid();
        this.email = member.getEmail();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.imgUrl = member.getImgUrl();
        this.reviewCnt = reviewCnt;
        this.createdDate = member.getCreatedDate();
    }
}
