package couch.camping.controller.member.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "회원 가입 요청 DTO")
public class MemberSaveRequestDto {

    @ApiModelProperty(required = true, value = "회원 닉네임", example = "asjdhaskudhasiudgflksd23")
    private String uid;
    @ApiModelProperty(required = true, value = "회원 닉네임", example = "abcd@gmail.com")
    private String email;
    @ApiModelProperty(required = true, value = "회원 닉네임", example = "김상운")
    private String name;
    @ApiModelProperty(required = true, value = "회원 닉네임", example = "abcd")
    private String nickname;
    @ApiModelProperty(required = true, value = "회원 닉네임", example = "www.img.com")
    private String imgUrl;
}
