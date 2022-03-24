package couch.camping.domain.post.service;

import couch.camping.controller.member.dto.response.MemberPostResponseDto;
import couch.camping.controller.post.dto.request.PostEditRequestDto;
import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.controller.post.dto.response.PostEditResponseDto;
import couch.camping.controller.post.dto.response.PostRetrieveResponseDto;
import couch.camping.controller.post.dto.response.PostWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostWriteResponseDto writePost(PostWriteRequestDto postWriteRequestDto, Member member);

    PostEditResponseDto editPost(Long postId, Member member, PostEditRequestDto postEditRequestDto);

    void likePost(Long postId, Member member);

    PostRetrieveResponseDto retrievePost(Long postId, String header);

    Page<PostRetrieveResponseDto> retrieveAllPost(String postType, Pageable pageable, String header);

    Page<PostRetrieveResponseDto> retrieveAllBestPost(Pageable pageable, String header);

    void deletePost(Long postId, Member member);

    Page<MemberPostResponseDto> retrieveMemberComment(Member member, Pageable pageable);
}
