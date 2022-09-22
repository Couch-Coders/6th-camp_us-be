package couch.camping.domain.post.repository.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static couch.camping.domain.member.entity.QMember.member;
import static couch.camping.domain.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findAllByIdWithFetchJoinMemberPaging(String postType, Pageable pageable) {
        List<Post> content = queryFactory
                .select(post)
                .from(post)
                .where(getEq(postType))
                .join(post.member, member).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdDate.desc())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(getEq(postType))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression getEq(String postType) {
        if (postType.equals("all"))
            return null;
        else
            return post.postType.eq(postType);
    }

    @Override
    public Page<Post> findAllBestPost(Pageable pageable) {
        List<Post> content = queryFactory
                .selectFrom(post)
                .where(post.likeCnt.goe(1))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.likeCnt.desc(), post.createdDate.desc())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.likeCnt.goe(1))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Post> findByMemberId(Long memberId, Pageable pageable) {
        List<Post> content = queryFactory
                .selectFrom(post)
                .where(post.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdDate.desc())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<Post> findByIdWithFetchJoinMember(Long postId) {
        Post content = queryFactory
                .select(post)
                .from(post)
                .join(post.member, member).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne();

        return Optional.ofNullable(content);
    }

    @Override
    public Long countByMemberId(Long memberId) {
        return queryFactory
                .select(post.count())
                .from(post)
                .where(post.member.id.eq(memberId))
                .fetchOne();
    }
}
