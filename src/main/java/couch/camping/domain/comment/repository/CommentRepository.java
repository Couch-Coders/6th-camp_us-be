package couch.camping.domain.comment.repository;

import couch.camping.controller.comment.dto.response.CommentRetrieveAllResponseDto;
import couch.camping.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
}
