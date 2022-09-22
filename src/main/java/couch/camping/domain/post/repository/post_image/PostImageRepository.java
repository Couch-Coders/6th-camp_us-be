package couch.camping.domain.post.repository.post_image;

import couch.camping.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long>, PostImageCustomRepository{
}
