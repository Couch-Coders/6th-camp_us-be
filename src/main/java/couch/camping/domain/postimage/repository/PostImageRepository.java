package couch.camping.domain.postimage.repository;

import couch.camping.domain.postimage.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from PostImage pi where pi.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);

    @Query("select pi from PostImage pi where pi.post.id = :postId")
    List<PostImage> findByPostId(@Param("postId") Long postId);
}
