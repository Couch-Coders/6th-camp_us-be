package couch.camping.domain.post.repository.post_image;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

import static couch.camping.domain.postimage.entity.QPostImage.postImage;

@RequiredArgsConstructor
public class PostImageCustomRepositoryImpl implements PostImageCustomRepository{

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public void deleteByPostId(Long postId) {
        queryFactory
                .delete(postImage)
                .where(postImage.post.id.eq(postId))
                .execute();

        em.flush();
        em.clear();
    }
}
