package couch.camping.domain.reviewlike.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import couch.camping.domain.review.entity.ReviewLike;


/**
 * QReviewLike is a Querydsl query type for ReviewLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewLike extends EntityPathBase<ReviewLike> {

    private static final long serialVersionUID = 1803669976L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewLike reviewLike = new QReviewLike("reviewLike");

    public final couch.camping.domain.base.QBaseTimeEntity _super = new couch.camping.domain.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final couch.camping.domain.member.entity.QMember member;

    public final couch.camping.domain.review.entity.QReview review;

    public QReviewLike(String variable) {
        this(ReviewLike.class, forVariable(variable), INITS);
    }

    public QReviewLike(Path<? extends ReviewLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewLike(PathMetadata metadata, PathInits inits) {
        this(ReviewLike.class, metadata, inits);
    }

    public QReviewLike(Class<? extends ReviewLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new couch.camping.domain.member.entity.QMember(forProperty("member")) : null;
        this.review = inits.isInitialized("review") ? new couch.camping.domain.review.entity.QReview(forProperty("review"), inits.get("review")) : null;
    }

}

