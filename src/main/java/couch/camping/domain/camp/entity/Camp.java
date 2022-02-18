package couch.camping.domain.camp.entity;

import couch.camping.domain.camplike.entity.CampLike;
import couch.camping.domain.review.entity.Review;
import lombok.*;

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

    private Integer likeCnt;

    private Integer reviewCnt;

    private Float avgRate;

    @OneToMany(mappedBy = "camp")
    private List<CampLike> campLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "camp")
    private List<Review> reviewList = new ArrayList<>();

    private String facltNm;
    private String lineIntro;
    @Lob
    private String intro;
    @Lob
    private String featureNm;
    private String induty;
    private String lctCl;
    private String doNm;
    private String sigunguNm;
    private String addr1;

    private float mapX;
    private float mapY;
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


    public void increaseCampLikeCnt() {
        this.likeCnt++;
    }

    public void decreaseCampLikeCnt() {
        this.likeCnt--;
    }

    public void increaseRate(int rate) {
        if (this.reviewCnt == 0 && this.avgRate == 0) {
            this.avgRate = (float)rate;
            ++this.reviewCnt;
        } else {
            float totalRate = this.avgRate * this.reviewCnt;
            totalRate += (float)rate;
            ++this.reviewCnt;
            this.avgRate = totalRate / reviewCnt;
        }
    }

    public void decreaseRate(int rate) {
        float totalRate = this.avgRate * this.reviewCnt;
        totalRate -= (float)rate;
        --this.reviewCnt;
        this.avgRate = totalRate / reviewCnt;
    }
}
