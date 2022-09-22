package couch.camping.domain.post.repository.post_like;

import couch.camping.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>, PostLikeCustomRepository {

}
