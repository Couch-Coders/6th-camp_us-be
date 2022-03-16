package couch.camping.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -375180262L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final couch.camping.domain.base.QBaseEntity _super = new couch.camping.domain.base.QBaseEntity(this);

    public final NumberPath<Integer> commentCnt = createNumber("commentCnt", Integer.class);

    public final ListPath<couch.camping.domain.comment.entity.Comment, couch.camping.domain.comment.entity.QComment> commentList = this.<couch.camping.domain.comment.entity.Comment, couch.camping.domain.comment.entity.QComment>createList("commentList", couch.camping.domain.comment.entity.Comment.class, couch.camping.domain.comment.entity.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> likeCnt = createNumber("likeCnt", Integer.class);

    public final couch.camping.domain.member.entity.QMember member;

    public final ListPath<couch.camping.domain.postimage.entity.PostImage, couch.camping.domain.postimage.entity.QPostImage> postImageList = this.<couch.camping.domain.postimage.entity.PostImage, couch.camping.domain.postimage.entity.QPostImage>createList("postImageList", couch.camping.domain.postimage.entity.PostImage.class, couch.camping.domain.postimage.entity.QPostImage.class, PathInits.DIRECT2);

    public final ListPath<couch.camping.domain.postlike.entity.PostLike, couch.camping.domain.postlike.entity.QPostLike> postLikeList = this.<couch.camping.domain.postlike.entity.PostLike, couch.camping.domain.postlike.entity.QPostLike>createList("postLikeList", couch.camping.domain.postlike.entity.PostLike.class, couch.camping.domain.postlike.entity.QPostLike.class, PathInits.DIRECT2);

    public final StringPath postType = createString("postType");

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new couch.camping.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

