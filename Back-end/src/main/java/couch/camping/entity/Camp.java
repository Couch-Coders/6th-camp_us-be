package couch.camping.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Camp {

    @Id @GeneratedValue
    @Column(name = "camp_id")
    private Long id;

    @Column(name = "like_cnt")
    private Integer like;

    private String facltNm;
    private String lineIntro;
    private String intro;
    private String featureNm;
    private String induty;
    private String lctCl;
    private String doNm;
    private String sigunguNm;
    private String addr1;
    private Integer mapX;
    private Long mapY;
    private String tel;
    private String direction;
    private String homepage;
    private String resveUrl;
    private Integer resveCl;
    private Integer autoSiteCo;
    private Integer glampSiteCo;
    private Integer caravSiteCo;
    private Integer sitedStnc;
    private Integer indvdlCaravSiteCo;
    private String glampInnerFclty;
    private String caravInnerFclty;
    private String operPdCl;
    private String operDeCl;
    private Boolean trlerAcmpnyAt;
    private Boolean caravAcmpnyAt;
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
    private Boolean animalCmgCl;
    private String tourEraCl;
    private String firstImageUrl;

}
