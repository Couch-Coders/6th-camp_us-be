package couch.camping.domain.post.repository.post_image;

public interface PostImageCustomRepository {
    long deleteByPostId(Long postId);
}
