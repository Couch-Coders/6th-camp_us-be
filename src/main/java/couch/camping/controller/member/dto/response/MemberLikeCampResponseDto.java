package couch.camping.controller.member.dto.response;

import couch.camping.domain.camp.entity.Camp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

//        ”campLikeId”:23
//        "campId": 224,
//        "memberId": 1,
//        ”campName”:”달빛캠핑장”,
//        ”imgUrl”:”www.img.com”
//        "createdDate": "2022-01-14T15:48:36.080486",
//        "checked": false
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "회원이 좋아요한 캠핑장 응답 DTO")
public class MemberLikeCampResponseDto {

    @ApiModelProperty(required = true, value = "캠프 ID", example = "2")
    private Long campId;
    @ApiModelProperty(required = true, value = "캠핑장 이름", example = "강원도 캠핑장")
    private String campName;
    @ApiModelProperty(required = true, value = "캠핑장 주소", example = "경상남도 창원시 의창구 북면 달천길 150 ")
    private String campAddr;
    @ApiModelProperty(required = true, value = "이미지 url", example = "www.img.com")
    private String imgUrl;
    @ApiModelProperty(required = true, value = "좋아요 여부", example = "true")
    private boolean likeCheck;

    public MemberLikeCampResponseDto(Camp camp) {
        this.campId = camp.getId();
        this.campName = camp.getFacltNm();
        this.campAddr = camp.getAddr1();
        this.imgUrl = camp.getFirstImageUrl();
    }
}
