package couch.camping.controller.camp.dto.response;

import couch.camping.domain.camp.entity.Camp;
import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "캠핑장 조회 응답 DTO")
public class CampResponseDto {

    private String facltNm;
    private String lineIntro;
    private String intro;
    private String featureNm;
    private String induty;
    private String lctCl;
    private String doNm;
    private String sigunguNm;
    private String addr1;
    private Float mapX;
    private Float mapY;
    private String tel;
    private String direction;
    private String homepage;
    private String resveUrl;
    private String resveCl;
    private Integer autoSiteCo;
    private Integer glampSiteCo;
    private Integer caravSiteCo;
    private Integer sitedStnc;
    private Integer indvdlCaravSiteCo;
    private String glampInnerFclty;
    private String caravInnerFclty;
    private String operPdCl;
    private String operDeCl;
    private String trlerAcmpnyAt;
    private String caravAcmpnyAt;
    private Integer toiletCo;
    private Integer swrmCo;
    private Integer wtrplCo;
    private String brazierCl;
    private String sbrsCl;
    private String sbrsEtc;
    private String posblFcltyCl;
    private String posblFcltyEtc;
    private Integer fireSensorCo;
    private Integer extshrCo;
    private String themaEnvrnCl;
    private String eqpmnLendCl;
    private String animalCmgCl;
    private String tourEraCl;
    private String firstImageUrl;
    private Integer likeCnt;
    private Float avgRate;


    public CampResponseDto(Camp camp) {
        this.facltNm = camp.getFacltNm();
        this.lineIntro = camp.getLineIntro();
        this.intro = camp.getIntro();
        this.featureNm = camp.getFeatureNm();
        this.induty = camp.getInduty();
        this.lctCl = camp.getLctCl();
        this.doNm = camp.getDoNm();
        this.sigunguNm = camp.getSigunguNm();
        this.addr1 = camp.getAddr1();
        this.mapX = camp.getMapX();
        this.mapY = camp.getMapY();
        this.tel = camp.getTel();
        this.direction = camp.getDirection();
        this.homepage = camp.getHomepage();
        this.resveUrl = camp.getResveUrl();
        this.resveCl = camp.getResveCl();
        this.autoSiteCo = camp.getAutoSiteCo();
        this.glampSiteCo = camp.getGlampSiteCo();
        this.caravSiteCo = camp.getCaravSiteCo();
        this.sitedStnc = camp.getSitedStnc();
        this.indvdlCaravSiteCo = camp.getIndvdlCaravSiteCo();
        this.glampInnerFclty = camp.getGlampInnerFclty();
        this.caravInnerFclty = camp.getCaravInnerFclty();
        this.operPdCl = camp.getOperPdCl();
        this.operDeCl = camp.getOperDeCl();
        this.trlerAcmpnyAt = camp.getTrlerAcmpnyAt();
        this.caravAcmpnyAt = camp.getCaravAcmpnyAt();
        this.toiletCo = camp.getToiletCo();
        this.swrmCo = camp.getSwrmCo();
        this.wtrplCo = camp.getWtrplCo();
        this.brazierCl = camp.getBrazierCl();
        this.sbrsCl = camp.getSbrsCl();
        this.sbrsEtc = camp.getSbrsEtc();
        this.posblFcltyCl = camp.getPosblFcltyCl();
        this.posblFcltyEtc = camp.getPosblFcltyEtc();
        this.fireSensorCo = camp.getFireSensorCo();
        this.extshrCo = camp.getExtshrCo();
        this.themaEnvrnCl = camp.getThemaEnvrnCl();
        this.eqpmnLendCl = camp.getEqpmnLendCl();
        this.animalCmgCl = camp.getAnimalCmgCl();
        this.tourEraCl = camp.getTourEraCl();
        this.firstImageUrl = camp.getFirstImageUrl();
        this.avgRate = camp.getAvgRate();
        this.likeCnt = camp.getLikeCnt();
    }
}
