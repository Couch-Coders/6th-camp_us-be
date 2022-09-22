package couch.camping.domain.post.repository.post_like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.post.entity.PostLike;
import couch.camping.domain.postlike.entity.QPostLike;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class PostLikeCustomRepositoryImpl implements PostLikeCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId) {

        PostLike postLike = queryFactory
                .select(QPostLike.postLike)
                .from(QPostLike.postLike)
                .where(QPostLike.postLike.member.id.eq(memberId), QPostLike.postLike.post.id.eq(postId))
                .fetchOne();

        return Optional.ofNullable(postLike);
    }
}
