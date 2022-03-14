package couch.camping.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.controller.comment.dto.response.CommentRetrieveAllResponseDto;
import couch.camping.domain.comment.entity.Comment;
import couch.camping.domain.comment.entity.QComment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static couch.camping.domain.comment.entity.QComment.comment;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findAllById(Long postId, Pageable pageable) {
        List<Comment> commentList = queryFactory.selectFrom(comment)
                .where(comment.post.id.eq(postId))
                .fetch();

        Long total = queryFactory.select(comment.count())
                .from(comment)
                .where(comment.post.id.eq(postId))
                .fetchOne();

        return new PageImpl(commentList, pageable, total);
    }
}
