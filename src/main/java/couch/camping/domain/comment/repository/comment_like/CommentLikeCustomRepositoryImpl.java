package couch.camping.domain.comment.repository.comment_like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.comment.entity.CommentLike;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static couch.camping.domain.comment.entity.QCommentLike.commentLike;


@RequiredArgsConstructor
public class CommentLikeCustomRepositoryImpl implements CommentLikeCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CommentLike> findByMemberIdAndCommentId(Long memberId, Long commentId) {
        CommentLike content = queryFactory
                .selectFrom(commentLike)
                .where(commentLike.member.id.eq(memberId), commentLike.comment.id.eq(commentId))
                .fetchOne();

        return Optional.ofNullable(content);
    }
}
