package couch.camping.domain.camp.entity;

import couch.camping.domain.camplike.entity.CampLike;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private Double rate;

    @Lob
    private String intro;

    @Lob
    private String featureNm;
    private String induty;
    private String lctCl;
    private String doNm;
    private String sigunguNm;
    private String addr1;
    private Float mapX;
    private Float mapY;
    private String tel;

    @Lob
    private String direction;
    private String homepage;

    @Lob
    private String resveUrl;
    private String resveCl;
    private int autoSiteCo;
    private int glampSiteCo;
    private int caravSiteCo;
    private int sitedStnc;
    private int indvdlCaravSiteCo;
    private String glampInnerFclty;
    private String caravInnerFclty;
    private String operPdCl;
    private String operDeCl;
    private String trlerAcmpnyAt;
    private String caravAcmpnyAt;
    private int toiletCo;
    private int swrmCo;
    private int wtrplCo;
    private String brazierCl;
    private String sbrsCl;
    private String sbrsEtc;
    private String posblFcltyCl;
    private String posblFcltyEtc;
    private int fireSensorCo;
    private int extshrCo;
    private String themaEnvrnCl;
    private String eqpmnLendCl;
    private String animalCmgCl;
    private String tourEraCl;
    private String firstImageUrl;

    private int campLikeCnt;

    @OneToMany(mappedBy = "camp")
    private List<CampLike> campLikeList = new ArrayList<>();

    public void increaseCampLikeCnt() {
        this.campLikeCnt++;
    }

    public void decreaseCampLikeCnt() {
        this.campLikeCnt--;
    }

    public void updateCampRate(Double rate) {
        this.rate = rate;
    }
}
