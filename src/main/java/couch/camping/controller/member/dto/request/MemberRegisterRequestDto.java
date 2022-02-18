package couch.camping.controller.member.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "회원 가입 요청 DTO")
public class MemberRegisterRequestDto {
    @ApiModelProperty(required = true, value = "회원 닉네임", example = "test")
    private String nickname;
}
