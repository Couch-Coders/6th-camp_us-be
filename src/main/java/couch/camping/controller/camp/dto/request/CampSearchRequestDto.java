package couch.camping.controller.camp.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@ApiModel(description = "캠핑장 검색 요청 DTO")
public class CampSearchRequestDto {
    //?rate=4.5
    // &doNm=경상도
    // &sigunguNm=창원군
    // &name=달빛캠핑장
    // &sort1=distance
    // &sort2=like
    // &tag=와이파이_전기_하수도_수영장
    // &x=3.1423
    // &y=3.141592
    @ApiModelProperty(required = true, value = "캠핑장 평균 평점", example = "4.35")
    private float avgRate;
    @ApiModelProperty(required = true, value = "도시", example = "경상도")
    private String doNm;
    @ApiModelProperty(required = true, value = "시.군.구", example = "창원군")
    private String sigunguNm;
    @ApiModelProperty(required = true, value = "캠핑장 이름", example = "달빛캠핑장")
    private String name;
    @ApiModelProperty(required = true, value = "X 좌표", example = "128.1423")
    private float mapX;
    @ApiModelProperty(required = true, value = "Y 좌표", example = "37.141592")
    private float mapY;
}
