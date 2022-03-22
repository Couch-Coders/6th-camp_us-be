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

    public final NumberPath<Integer> commentCnt = createNumber("commentCnt", Integer.class);

    public final ListPath<couch.camping.domain.comment.entity.Comment, couch.camping.domain.comment.entity.QComment> commentList = this.<couch.camping.domain.comment.entity.Comment, couch.camping.domain.comment.entity.QComment>createList("commentList", couch.camping.domain.comment.entity.Comment.class, couch.camping.domain.comment.entity.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = createDateTime("lastModifiedDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> likeCnt = createNumber("likeCnt", Integer.class);

    public final couch.camping.domain.member.entity.QMember member;

    public final ListPath<couch.camping.domain.postimage.entity.PostImage, couch.camping.domain.postimage.entity.QPostImage> postImageList = this.<couch.camping.domain.postimage.entity.PostImage, couch.camping.domain.postimage.entity.QPostImage>createList("postImageList", couch.camping.domain.postimage.entity.PostImage.class, couch.camping.domain.postimage.entity.QPostImage.class, PathInits.DIRECT2);

    public final ListPath<couch.camping.domain.postlike.entity.PostLike, couch.camping.domain.postlike.entity.QPostLike> postLikeList = this.<couch.camping.domain.postlike.entity.PostLike, couch.camping.domain.postlike.entity.QPostLike>createList("postLikeList", couch.camping.domain.postlike.entity.PostLike.class, couch.camping.domain.postlike.entity.QPostLike.class, PathInits.DIRECT2);

    public final StringPath postType = createString("postType");

    public final StringPath title = createString("title");

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

