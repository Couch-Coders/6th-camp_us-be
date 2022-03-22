package couch.camping.domain.comment.service;

import couch.camping.controller.comment.dto.request.CommentEditRequestDto;
import couch.camping.controller.comment.dto.request.CommentWriteRequestDto;
import couch.camping.controller.comment.dto.response.CommentEditResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveResponseDto;
import couch.camping.controller.comment.dto.response.CommentWriteResponseDto;
import couch.camping.controller.member.dto.response.MemberCommentsResponseDto;
import couch.camping.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentWriteResponseDto writeComment(CommentWriteRequestDto commentWriteRequestDto, Member member, Long postId);

    CommentEditResponseDto editComment(CommentEditRequestDto commentEditRequestDto, Member member, Long commentId);

    void likeComment(Long commentId, Member member);

    CommentRetrieveResponseDto retrieveComment(Long commentId, String header);

    Page<CommentRetrieveResponseDto> retrieveAllComment(Long postId, String header, Pageable pageable);

    void deleteComment(Long commentId, Member member);

    Page<MemberCommentsResponseDto> retrieveMemberComment(Member member, Pageable pageable);
}
