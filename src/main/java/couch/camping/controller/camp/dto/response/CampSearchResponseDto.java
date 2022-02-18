package couch.camping.controller.camp.dto.response;

import couch.camping.domain.camp.entity.Camp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "캠핑장 검색 응답 DTO")
public class CampSearchResponseDto {

    @ApiModelProperty(required = true, value = "캠핑장 ID", example = "2598")
    private Long campId;
    @ApiModelProperty(required = true, value = "캠핑장 주소", example = "경상북도 문경시 마성면 구랑로 78-11 ")
    private String addr1;
    @ApiModelProperty(required = true, value = "캠핑장 이름", example = "달빛 캠핑장")
    private String facltNm;
    @ApiModelProperty(required = true, value = "캠핑장 이미지 주소", example = "4.35")
    private String firstImageUrl;
    @ApiModelProperty(required = true, value = "캠핑장 한줄 소개", example = "자연 경관이 좋아요")
    private String lineIntro;
    @ApiModelProperty(required = true, value = "캠핑장 좋아요 수", example = "28")
    private int like;
    @ApiModelProperty(required = true, value = "캠핑장 평균 평점", example = "4.35")
    private float rate;
    @ApiModelProperty(required = true, value = "X 좌표", example = "128.1423")
    private float mapX;
    @ApiModelProperty(required = true, value = "Y 좌표", example = "37.141592")
    private float mapY;

    public CampSearchResponseDto(Camp camp) {
        this.campId = camp.getId();
        this.addr1 = camp.getAddr1();
        this.facltNm = camp.getFacltNm();
        this.firstImageUrl = camp.getFirstImageUrl();
        this.lineIntro = camp.getLineIntro();
        this.rate = camp.getAvgRate();
        this.like = camp.getLikeCnt();
        this.mapX = camp.getMapX();
        this.mapY = camp.getMapY();
    }
}
