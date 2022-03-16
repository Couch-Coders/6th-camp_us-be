package couch.camping.domain.comment.repository;

import couch.camping.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentCustomRepository {

    Page<Comment> findAllByIdWithFetchJoinMemberPaging(Long postId, Pageable pageable);
    Optional<Comment> findIdWithFetchJoinMember(Long postId);
}
