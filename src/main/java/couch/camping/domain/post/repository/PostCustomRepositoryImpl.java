package couch.camping.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static couch.camping.domain.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository{

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Page<Post> findAllByIdWithPaging(String postType, Pageable pageable) {
        List<Post> content = queryFactory
                .select(post)
                .from(post)
                .where(post.postType.eq(postType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdDate.desc())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.postType.eq(postType))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
