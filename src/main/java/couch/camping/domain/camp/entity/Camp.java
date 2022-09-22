package couch.camping.domain.camp.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter
@Setter
@Builder
public class Camp {

    @Id @GeneratedValue
    @Column(name = "camp_id")
    private Long id;

    private int likeCnt; // 0

    private int reviewCnt; // 0

    private float avgRate; // 0

    @OneToMany(mappedBy = "camp")
    private List<CampLike> campLikeList = new ArrayList<>();

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

    public Camp(Object[] o) {
        this.id = ((BigInteger)o[0]).longValue();
        this.addr1 = (String)o[1];
        this.animalCmgCl = (String)o[2];
        this.autoSiteCo = (int)o[3];
        this.avgRate = (float)o[4];
        this.brazierCl = (String)o[5];
        this.caravAcmpnyAt = (String)o[6];
        this.caravInnerFclty = (String) o[7];
        this.caravSiteCo = (int)o[8];
        this.direction = (String) o[9];
        this.doNm = (String) o[10];
        this.eqpmnLendCl = (String) o[11];
        this.extshrCo = (int) o[12];
        this.facltNm = (String) o[13];
        this.featureNm = (String) o[14];
        this.fireSensorCo = (int) o[15];
        this.firstImageUrl = (String) o[16];
        this.glampInnerFclty = (String) o[17];
        this.glampSiteCo = (int) o[18];
        this.homepage = (String) o[19];
        this.induty = (String) o[20];
        this.indvdlCaravSiteCo = (int) o[21];
        this.intro = (String) o[22];
        this.lctCl = (String) o[23];
        this.likeCnt = (int) o[24];
        this.lineIntro = (String) o[25];
        this.mapX = (float) o[26];
        this.mapY = (float) o[27];
        this.operDeCl = (String) o[28];
        this.operPdCl = (String) o[29];
        this.posblFcltyCl = (String) o[30];
        this.posblFcltyEtc = (String) o[31];
        this.resveCl = (String) o[32];
        this.resveUrl = (String) o[33];
        this.reviewCnt = (int) o[34];
        this.sbrsCl = (String) o[35];
        this.sbrsEtc = (String) o[36];
        this.sigunguNm = (String) o[37];
        this.sitedStnc = (int) o[38];
        this.swrmCo = (int) o[39];
        this.tel = (String) o[40];
        this.themaEnvrnCl = (String) o[41];
        this.toiletCo = (int) o[42];
        this.tourEraCl = (String) o[43];
        this.trlerAcmpnyAt = (String) o[44];
        this.wtrplCo = (int) o[45];
    }

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
        if (totalRate == 0) this.avgRate = 0;
        else this.avgRate = totalRate / reviewCnt;
    }

    public void editDate(int beforeRate, int afterRate) {
        float totalRate = this.avgRate * this.reviewCnt;
        totalRate -= beforeRate;
        totalRate += afterRate;
        this.avgRate = totalRate / reviewCnt;
    }

}
