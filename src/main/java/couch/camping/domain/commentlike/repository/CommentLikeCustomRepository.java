package couch.camping.domain.commentlike.repository;

import couch.camping.domain.commentlike.entity.CommentLike;

import java.util.Optional;

public interface CommentLikeCustomRepository {

    Optional<CommentLike> findByMemberIdAndCommentId(Long memberId, Long commentId);
}
