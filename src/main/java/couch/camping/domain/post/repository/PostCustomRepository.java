package couch.camping.domain.post.repository;

import couch.camping.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCustomRepository {

    Page<Post> findAllByIdWithPaging(String postType, Pageable pageable);
}
