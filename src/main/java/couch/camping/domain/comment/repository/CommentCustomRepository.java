package couch.camping.domain.comment.repository;

import couch.camping.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentCustomRepository {

    Page<Comment> findAllByIdWithPaging(Long postId, Pageable pageable);
}
