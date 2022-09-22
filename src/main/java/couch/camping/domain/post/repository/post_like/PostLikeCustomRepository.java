package couch.camping.domain.post.repository.post_like;

import couch.camping.domain.post.entity.PostLike;

import java.util.Optional;

public interface PostLikeCustomRepository {

    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);
}
