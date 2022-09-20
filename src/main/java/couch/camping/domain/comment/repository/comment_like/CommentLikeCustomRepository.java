package couch.camping.domain.comment.repository.comment_like;

import couch.camping.domain.comment.entity.CommentLike;

import java.util.Optional;

public interface CommentLikeCustomRepository {

    Optional<CommentLike> findByMemberIdAndCommentId(Long memberId, Long commentId);
}
