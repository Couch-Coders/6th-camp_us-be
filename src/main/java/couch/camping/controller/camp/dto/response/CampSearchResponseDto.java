package couch.camping.controller.camp.dto.response;

import couch.camping.domain.camp.entity.Camp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampSearchResponseDto {
    private String addr1;
    private String facltNm;
    private String firstImageUrl;
    private String lineIntro;
    private Integer like;
    private float mapX;
    private float mapY;

    public CampSearchResponseDto(Camp camp) {
        this.addr1 = camp.getAddr1();
        this.facltNm = camp.getFacltNm();
        this.firstImageUrl = camp.getFirstImageUrl();
        this.lineIntro = camp.getLineIntro();
        this.like = camp.getLike();
        this.mapX = camp.getMapX();
        this.mapY = camp.getMapY();
    }
}
