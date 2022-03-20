package couch.camping.domain.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotification is a Querydsl query type for Notification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotification extends EntityPathBase<Notification> {

    private static final long serialVersionUID = -1144776144L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotification notification = new QNotification("notification");

    public final couch.camping.domain.base.QBaseTimeEntity _super = new couch.camping.domain.base.QBaseTimeEntity(this);

    public final couch.camping.domain.comment.entity.QComment comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isChecked = createBoolean("isChecked");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final couch.camping.domain.member.entity.QMember member;

    public final couch.camping.domain.member.entity.QMember ownerMember;

    public final couch.camping.domain.post.entity.QPost post;

    public final couch.camping.domain.review.entity.QReview review;

    public final couch.camping.domain.comment.entity.QComment writeComment;

    public QNotification(String variable) {
        this(Notification.class, forVariable(variable), INITS);
    }

    public QNotification(Path<? extends Notification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotification(PathMetadata metadata, PathInits inits) {
        this(Notification.class, metadata, inits);
    }

    public QNotification(Class<? extends Notification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new couch.camping.domain.comment.entity.QComment(forProperty("comment"), inits.get("comment")) : null;
        this.member = inits.isInitialized("member") ? new couch.camping.domain.member.entity.QMember(forProperty("member")) : null;
        this.ownerMember = inits.isInitialized("ownerMember") ? new couch.camping.domain.member.entity.QMember(forProperty("ownerMember")) : null;
        this.post = inits.isInitialized("post") ? new couch.camping.domain.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
        this.review = inits.isInitialized("review") ? new couch.camping.domain.review.entity.QReview(forProperty("review"), inits.get("review")) : null;
        this.writeComment = inits.isInitialized("writeComment") ? new couch.camping.domain.comment.entity.QComment(forProperty("writeComment"), inits.get("writeComment")) : null;
    }

}

