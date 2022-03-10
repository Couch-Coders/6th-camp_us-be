package couch.camping.domain.postimage.repository;

import couch.camping.domain.postimage.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long>, PostImageCustomRepository{
}
