package couch.camping.domain.comment.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static couch.camping.domain.comment.entity.QComment.comment;
import static couch.camping.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findAllByPostIdWithFetchJoinMemberPaging(Long postId, Pageable pageable) {
        List<Comment> commentList = queryFactory
                .selectFrom(comment)
                .join(comment.member, member).fetchJoin()
                .where(comment.post.id.eq(postId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdDate.asc())
                .fetch();

        Long total = queryFactory.select(comment.count())
                .from(comment)
                .where(comment.post.id.eq(postId))
                .fetchOne();

        return new PageImpl(commentList, pageable, total);
    }

    @Override
    public Optional<Comment> findByIdWithFetchJoinMember(Long commentId) {

        Comment content = queryFactory
                .selectFrom(comment)
                .join(comment.member, member).fetchJoin()
                .where(comment.id.eq(commentId))
                .fetchOne();

        return Optional.ofNullable(content);
    }

    @Override
    public Page<Comment> findByMemberId(Long memberId, Pageable pageable) {
        List<Comment> commentList = queryFactory
                .selectFrom(comment)
                .where(comment.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdDate.desc())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.member.id.eq(memberId))
                .fetchOne();
        return new PageImpl<>(commentList, pageable, total);
    }

    @Override
    public Long countByMemberId(Long memberId) {
        return queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.member.id.eq(memberId))
                .fetchOne();
    }
}
