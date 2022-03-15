package couch.camping.domain.postlike.repository;

import couch.camping.domain.postlike.entity.PostLike;

import java.util.Optional;

public interface PostLikeCustomRepository {

    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);
}
