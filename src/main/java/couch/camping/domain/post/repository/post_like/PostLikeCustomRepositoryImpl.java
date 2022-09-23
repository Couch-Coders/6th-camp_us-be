package couch.camping.domain.post.repository.post_like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.post.entity.PostLike;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static couch.camping.domain.post.entity.QPostLike.postLike;

@RequiredArgsConstructor
public class PostLikeCustomRepositoryImpl implements PostLikeCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId) {

        PostLike content = queryFactory
                .select(postLike)
                .from(postLike)
                .where(postLike.member.id.eq(memberId), postLike.post.id.eq(postId))
                .fetchOne();

        return Optional.ofNullable(content);
    }
}
