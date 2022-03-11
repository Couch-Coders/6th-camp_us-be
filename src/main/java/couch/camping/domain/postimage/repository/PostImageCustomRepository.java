package couch.camping.domain.postimage.repository;

import couch.camping.domain.postimage.entity.PostImage;

import java.util.List;

public interface PostImageCustomRepository {
    void deleteByPostId(Long postId);
    List<PostImage> findByPostId(Long postId);
}
