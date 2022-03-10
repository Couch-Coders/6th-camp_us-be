package couch.camping.domain.postimage.repository;

import couch.camping.domain.postimage.entity.PostImage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageCustomRepository {
    void deleteByPostId(Long postId);
    List<PostImage> findByPostId(Long postId);
}
