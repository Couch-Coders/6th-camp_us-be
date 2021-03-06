package couch.camping.domain.comment.service;

import couch.camping.controller.comment.dto.request.CommentEditRequestDto;
import couch.camping.controller.comment.dto.request.CommentWriteRequestDto;
import couch.camping.controller.comment.dto.response.CommentEditResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveLoginResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveResponseDto;
import couch.camping.controller.comment.dto.response.CommentWriteResponseDto;
import couch.camping.controller.member.dto.response.MemberCommentsResponseDto;
import couch.camping.domain.comment.entity.Comment;
import couch.camping.domain.comment.repository.CommentRepository;
import couch.camping.domain.commentlike.entity.CommentLike;
import couch.camping.domain.commentlike.repository.CommentLikeRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.notification.entity.Notification;
import couch.camping.domain.notification.repository.NotificationRepository;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.repository.PostRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Profile("local")
@Service
@RequiredArgsConstructor
public class CommentServiceLocalImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final UserDetailsService userDetailsService;
    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public CommentWriteResponseDto writeComment(CommentWriteRequestDto commentWriteRequestDto, Member member, Long postId) {

        Post findPost = findPostOrElseThrow(postId);
        Comment saveComment = saveComment(commentWriteRequestDto, member, findPost);

        if (isNotSameMember(member, findPost.getMember()))
            saveNotification(createCommentWriteNotification(findPost, saveComment));

        return new CommentWriteResponseDto(saveComment);
    }

    private void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    private Comment saveComment(CommentWriteRequestDto commentWriteRequestDto, Member member, Post post) {
        return commentRepository.save(createComment(commentWriteRequestDto, member, post));
    }

    private boolean isNotSameMember(Member loginMember, Member ownerMember) {
        return loginMember != ownerMember;
    }

    private Notification createCommentWriteNotification(Post findPost, Comment saveComment) {
        return Notification.builder()
                        .writeComment(saveComment)
                        .member(saveComment.getMember())//???????????? ?????? ?????? ?????????
                        .ownerMember(findPost.getMember())//???????????? ??????
                        .build();
    }

    private Comment createComment(CommentWriteRequestDto commentWriteRequestDto, Member member, Post post) {
        return Comment.builder()
                .content(commentWriteRequestDto.getContent())
                .member(member)
                .post(post)
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    private Post findPostOrElseThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "????????? ID ??? ?????? ???????????? ????????????.");
                });
    }

    @Transactional
    @Override
    public CommentEditResponseDto editComment(CommentEditRequestDto commentEditRequestDto, Member member, Long commentId) {
        Comment findComment = findCommentOrElseThrow(commentId);

        validateAuthorization(member, findComment.getMember());

        findComment.editComment(commentEditRequestDto.getContent());

        return new CommentEditResponseDto(findComment);
    }

    private void validateAuthorization(Member loginMember, Member ownerMember) {
        if (isNotSameMember(loginMember, ownerMember))
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "?????? ????????? ????????? ????????????.");
    }

    private Comment findCommentOrElseThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_COMMENT, "?????? ID??? ?????? ????????? ????????????.");
                });
    }

    @Transactional
    @Override
    public int likeComment(Long commentId, Member member) {
        Comment findComment = findCommentOrElseThrow(commentId);
        calculateCommentLikeCnt(member, findComment);
        return findComment.getLikeCnt();
    }

    private void calculateCommentLikeCnt(Member member, Comment comment) {
        Optional<CommentLike> findCommentLike = getOptionalCommentLike(member, comment);

        if (isLikedComment(findCommentLike))
            decreaseCommentLikeCnt(comment, findCommentLike);
         else {
            increaseCommentLikeCnt(member, comment);
            if (isNotSameMember(member, comment.getMember()))
                checkToSaveNotification(member, comment);
        }
    }

    private Optional<CommentLike> getOptionalCommentLike(Member member, Comment comment) {
        return commentLikeRepository.findByMemberIdAndCommentId(member.getId(), comment.getId());
    }


    private void checkToSaveNotification(Member member, Comment findComment) {
        if (!isExistNotification(findComment.getId(), member))
            saveNotification(createLikeCommentNotification(member, findComment));
    }

    private boolean isLikedComment(Optional<CommentLike> findCommentLike) {
        return findCommentLike.isPresent();
    }

    private boolean isExistNotification(Long commentId, Member member) {
        return notificationRepository.findByMemberIdAndCommentId(member.getId(), commentId).isPresent();
    }

    private Notification createLikeCommentNotification(Member member, Comment findComment) {
        return Notification.builder()
                .comment(findComment)//?????? ?????????
                .member(member)//???????????? ?????? ?????? ?????????
                .ownerMember(findComment.getMember())//???????????? ??????
                .build();
    }

    private void increaseCommentLikeCnt(Member member, Comment findComment) {
        findComment.increaseLikeCnt();
        saveCommentLike(member, findComment);
    }

    private CommentLike saveCommentLike(Member member, Comment findComment) {
        return commentLikeRepository.save(createCommentLike(member, findComment));
    }

    private CommentLike createCommentLike(Member member, Comment findComment) {
        return CommentLike.builder()
                .comment(findComment)
                .member(member)
                .build();
    }

    private void decreaseCommentLikeCnt(Comment findComment, Optional<CommentLike> findCommentLike) {
        findComment.decreaseLikeCnt();
        deleteCommentLike(findCommentLike.get().getId());
    }

    private void deleteCommentLike(Long commentLikeId) {
        commentLikeRepository.deleteById(commentLikeId);
    }

    @Override
    public CommentRetrieveResponseDto retrieveComment(Long commentId, String header) {
        Comment findComment = findIdWithFetchJoinMemberOrElseThrow(commentId);
        if (header == null) {
            return createCommentRetrieveResponseDto(findComment);
        } else {
            Member member = getMemberOrElseThrow(header);
            return createCommentRetrieveLoginResponseDto(findComment, member);
        }
    }

    private CommentRetrieveResponseDto createCommentRetrieveResponseDto(Comment comment) {
        return new CommentRetrieveResponseDto(comment);
    }

    private CommentRetrieveLoginResponseDto createCommentRetrieveLoginResponseDto(Comment comment, Member member) {
        for (CommentLike commentLike : comment.getCommentLikeList()) {
            if (isNotSameMember(commentLike.getMember(), member))
                return new CommentRetrieveLoginResponseDto(comment, true);
        }
        return new CommentRetrieveLoginResponseDto(comment, false);
    }

    private Member getMemberOrElseThrow(String header) {
        Member member;
        try {
            member = (Member) userDetailsService.loadUserByUsername(header);
        } catch (UsernameNotFoundException e) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "????????? ???????????? ????????? ???????????? ????????????.");
        }
        return member;
    }

    private Comment findIdWithFetchJoinMemberOrElseThrow(Long commentId) {
        return commentRepository.findIdWithFetchJoinMember(commentId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_COMMENT, "?????? ID??? ?????? ????????? ????????????.");
                });
    }


    @Override
    public Page<CommentRetrieveResponseDto> retrieveAllComment(Long postId, String header, Pageable pageable) {

        Page<Comment> commentPage = findAllComment(postId, pageable);
        if (header == null)
            return createAllCommentDto(commentPage);
        else
            return createAllLoginCommentDto(commentPage, getMemberOrElseThrow(header));
    }

    private Page<CommentRetrieveResponseDto> createAllLoginCommentDto(Page<Comment> commentPage, Member member) {
        return commentPage
                .map(comment -> mapToLoginCommentDto(member, comment));
    }

    private CommentRetrieveLoginResponseDto mapToLoginCommentDto(Member member, Comment comment) {
        for (CommentLike commentLike : comment.getCommentLikeList()) {
            if (commentLike.getMember() == member)
                return new CommentRetrieveLoginResponseDto(comment, true);
        }
        return new CommentRetrieveLoginResponseDto(comment, false);
    }

    private Page<CommentRetrieveResponseDto> createAllCommentDto(Page<Comment> commentPage) {
        return commentPage
                .map(comment -> createCommentRetrieveResponseDto(comment));
    }

    private Page<Comment> findAllComment(Long postId, Pageable pageable) {
        return commentRepository.findAllByIdWithFetchJoinMemberPaging(postId, pageable);
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, Member member) {
        validateAuthorization(member, findCommentOrElseThrow(commentId).getMember());
        commentRepository.deleteById(commentId);
    }

    @Override
    public Page<MemberCommentsResponseDto> retrieveMemberComment(Member member, Pageable pageable) {
        return commentRepository.findByMemberId(member.getId(), pageable)
                .map(comment -> new MemberCommentsResponseDto(comment));
    }

    @Override
    public long countMemberComments(Long id) {
        return commentRepository.countByMemberId(id);
    }
}
