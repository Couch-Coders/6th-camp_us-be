package couch.camping.domain.commentlike.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.commentlike.entity.CommentLike;
import couch.camping.domain.commentlike.entity.QCommentLike;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static couch.camping.domain.commentlike.entity.QCommentLike.*;

@RequiredArgsConstructor
public class CommentLikeCustomRepositoryImpl implements CommentLikeCustomRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<CommentLike> findByMemberIdAndCommentId(Long memberId, Long commentId) {
        CommentLike commentLike = queryFactory
                .selectFrom(QCommentLike.commentLike)
                .where(QCommentLike.commentLike.member.id.eq(memberId), QCommentLike.commentLike.comment.id.eq(commentId))
                .fetchOne();

        return Optional.ofNullable(commentLike);
    }
}
