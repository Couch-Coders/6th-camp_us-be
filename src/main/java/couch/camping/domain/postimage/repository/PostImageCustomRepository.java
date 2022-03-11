package couch.camping.domain.postimage.repository;

public interface PostImageCustomRepository {
    void deleteByPostId(Long postId);
}
