package couch.camping.controller.camp.dto.response;

import couch.camping.domain.camp.entity.Camp;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampSearchResponseDto {

    private Long campId;
    private String addr1;
    private String facltNm;
    private String firstImageUrl;
    private String lineIntro;
    private Integer like;
    private float rate;
    private float mapX;
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
