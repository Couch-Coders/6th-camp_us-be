package couch.camping.domain.commentlike.repository;

import couch.camping.domain.commentlike.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>, CommentLikeCustomRepository {
}
