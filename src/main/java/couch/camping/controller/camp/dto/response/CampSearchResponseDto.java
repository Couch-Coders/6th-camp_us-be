package couch.camping.controller.camp.dto.response;

import couch.camping.domain.camp.entity.Camp;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampSearchResponseDto {
    private String addr1;
    private String facltNm;
    private String firstImageUrl;
    private String lineIntro;
    private Integer like;
    private Double rate;
    private float mapX;
    private float mapY;

    public CampSearchResponseDto(Camp camp) {
        this.addr1 = camp.getAddr1();
        this.facltNm = camp.getFacltNm();
        this.firstImageUrl = camp.getFirstImageUrl();
        this.lineIntro = camp.getLineIntro();
        this.rate = camp.getRate();
        this.like = camp.getLike();
        this.mapX = camp.getMapX();
        this.mapY = camp.getMapY();
    }
}
