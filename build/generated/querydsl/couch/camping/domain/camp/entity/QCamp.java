package couch.camping.domain.camp.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCamp is a Querydsl query type for Camp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCamp extends EntityPathBase<Camp> {

    private static final long serialVersionUID = -2047463844L;

    public static final QCamp camp = new QCamp("camp");

    public final StringPath addr1 = createString("addr1");

    public final StringPath animalCmgCl = createString("animalCmgCl");

    public final NumberPath<Integer> autoSiteCo = createNumber("autoSiteCo", Integer.class);

    public final NumberPath<Float> avgRate = createNumber("avgRate", Float.class);

    public final StringPath brazierCl = createString("brazierCl");

    public final ListPath<couch.camping.domain.camplike.entity.CampLike, couch.camping.domain.camplike.entity.QCampLike> campLikeList = this.<couch.camping.domain.camplike.entity.CampLike, couch.camping.domain.camplike.entity.QCampLike>createList("campLikeList", couch.camping.domain.camplike.entity.CampLike.class, couch.camping.domain.camplike.entity.QCampLike.class, PathInits.DIRECT2);

    public final StringPath caravAcmpnyAt = createString("caravAcmpnyAt");

    public final StringPath caravInnerFclty = createString("caravInnerFclty");

    public final NumberPath<Integer> caravSiteCo = createNumber("caravSiteCo", Integer.class);

    public final StringPath direction = createString("direction");

    public final StringPath doNm = createString("doNm");

    public final StringPath eqpmnLendCl = createString("eqpmnLendCl");

    public final NumberPath<Integer> extshrCo = createNumber("extshrCo", Integer.class);

    public final StringPath facltNm = createString("facltNm");

    public final StringPath featureNm = createString("featureNm");

    public final NumberPath<Integer> fireSensorCo = createNumber("fireSensorCo", Integer.class);

    public final StringPath firstImageUrl = createString("firstImageUrl");

    public final StringPath glampInnerFclty = createString("glampInnerFclty");

    public final NumberPath<Integer> glampSiteCo = createNumber("glampSiteCo", Integer.class);

    public final StringPath homepage = createString("homepage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath induty = createString("induty");

    public final NumberPath<Integer> indvdlCaravSiteCo = createNumber("indvdlCaravSiteCo", Integer.class);

    public final StringPath intro = createString("intro");

    public final StringPath lctCl = createString("lctCl");

    public final NumberPath<Integer> likeCnt = createNumber("likeCnt", Integer.class);

    public final StringPath lineIntro = createString("lineIntro");

    public final NumberPath<Float> mapX = createNumber("mapX", Float.class);

    public final NumberPath<Float> mapY = createNumber("mapY", Float.class);

    public final StringPath operDeCl = createString("operDeCl");

    public final StringPath operPdCl = createString("operPdCl");

    public final StringPath posblFcltyCl = createString("posblFcltyCl");

    public final StringPath posblFcltyEtc = createString("posblFcltyEtc");

    public final StringPath resveCl = createString("resveCl");

    public final StringPath resveUrl = createString("resveUrl");

    public final NumberPath<Integer> reviewCnt = createNumber("reviewCnt", Integer.class);

    public final StringPath sbrsCl = createString("sbrsCl");

    public final StringPath sbrsEtc = createString("sbrsEtc");

    public final StringPath sigunguNm = createString("sigunguNm");

    public final NumberPath<Integer> sitedStnc = createNumber("sitedStnc", Integer.class);

    public final NumberPath<Integer> swrmCo = createNumber("swrmCo", Integer.class);

    public final StringPath tel = createString("tel");

    public final StringPath themaEnvrnCl = createString("themaEnvrnCl");

    public final NumberPath<Integer> toiletCo = createNumber("toiletCo", Integer.class);

    public final StringPath tourEraCl = createString("tourEraCl");

    public final StringPath trlerAcmpnyAt = createString("trlerAcmpnyAt");

    public final NumberPath<Integer> wtrplCo = createNumber("wtrplCo", Integer.class);

    public QCamp(String variable) {
        super(Camp.class, forVariable(variable));
    }

    public QCamp(Path<? extends Camp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCamp(PathMetadata metadata) {
        super(Camp.class, metadata);
    }

}

