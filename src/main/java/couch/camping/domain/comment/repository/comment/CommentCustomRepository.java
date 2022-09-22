package couch.camping.domain.comment.repository.comment;

import couch.camping.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentCustomRepository {

    Page<Comment> findAllByPostIdWithFetchJoinMemberPaging(Long postId, Pageable pageable);

    Optional<Comment> findByIdWithFetchJoinMember(Long commentId);

    Page<Comment> findByMemberId(Long memberId, Pageable pageable);

    Long countByMemberId(Long memberId);
}
