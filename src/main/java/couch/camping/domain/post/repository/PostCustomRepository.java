package couch.camping.domain.post.repository;

import couch.camping.controller.member.dto.response.MemberPostResponseDto;
import couch.camping.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostCustomRepository {
    Optional<Post> findByIdWithFetchJoinMember(Long postId);
    Page<Post> findAllByIdWithFetchJoinMemberPaging(String postType, Pageable pageable);
    Page<Post> findAllBestPost(Pageable pageable);
    Page<Post> findByMemberId(Long memberId, Pageable pageable);
}
