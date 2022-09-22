package couch.camping.domain.camplike.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import couch.camping.domain.camp.entity.CampLike;


/**
 * QCampLike is a Querydsl query type for CampLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCampLike extends EntityPathBase<CampLike> {

    private static final long serialVersionUID = -1239222614L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCampLike campLike = new QCampLike("campLike");

    public final couch.camping.domain.base.QBaseTimeEntity _super = new couch.camping.domain.base.QBaseTimeEntity(this);

    public final couch.camping.domain.camp.entity.QCamp camp;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final couch.camping.domain.member.entity.QMember member;

    public QCampLike(String variable) {
        this(CampLike.class, forVariable(variable), INITS);
    }

    public QCampLike(Path<? extends CampLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCampLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCampLike(PathMetadata metadata, PathInits inits) {
        this(CampLike.class, metadata, inits);
    }

    public QCampLike(Class<? extends CampLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.camp = inits.isInitialized("camp") ? new couch.camping.domain.camp.entity.QCamp(forProperty("camp")) : null;
        this.member = inits.isInitialized("member") ? new couch.camping.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

