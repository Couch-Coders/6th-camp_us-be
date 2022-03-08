package couch.camping.domain.postlike.repository;

import couch.camping.domain.postlike.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>, PostLikeCustomRepository {

}
