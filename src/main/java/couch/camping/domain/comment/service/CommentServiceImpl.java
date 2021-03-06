package couch.camping.domain.comment.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
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
import java.util.List;
import java.util.Optional;

@Profile("prod")
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final UserDetailsService userDetailsService;
    private final FirebaseAuth firebaseAuth;
    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public CommentWriteResponseDto writeComment(CommentWriteRequestDto commentWriteRequestDto, Member member, Long postId) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "????????? ID ??? ?????? ???????????? ????????????.");
                });

        Comment comment = Comment.builder()
                .content(commentWriteRequestDto.getContent())
                .member(member)
                .post(findPost)
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Comment saveComment = commentRepository.save(comment);

        if (findPost.getMember() != member) { // ????????? ???????????? ?????? ???????????? ????????? ?????? ??????

            Notification notification = Notification.builder()
                    .writeComment(saveComment)
                    .member(member)//???????????? ?????? ?????? ?????????
                    .ownerMember(findPost.getMember())//???????????? ??????
                    .build();

            notificationRepository.save(notification);
        }

        return new CommentWriteResponseDto(saveComment);
    }

    @Transactional
    @Override
    public CommentEditResponseDto editComment(CommentEditRequestDto commentEditRequestDto, Member member, Long commentId) {

        Comment findComment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> {throw new CustomException(ErrorCode.NOT_FOUND_COMMENT, "?????? ID??? ?????? ????????? ????????????.");
                });

        if (findComment.getMember() != member){
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "?????? ????????? ????????? ????????????.");
        }

        findComment.editComment(commentEditRequestDto.getContent());

        return new CommentEditResponseDto(findComment);
    }

    @Transactional
    @Override
    public int likeComment(Long commentId, Member member) {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_COMMENT, "?????? ID??? ?????? ????????? ????????????.");
                });

        Optional<CommentLike> findCommentLike = commentLikeRepository.findByMemberIdAndCommentId(member.getId(), findComment.getId());

        if (findCommentLike.isPresent()){
            findComment.decreaseLikeCnt();
            commentLikeRepository.deleteById(findCommentLike.get().getId());
        } else {
            findComment.increaseLikeCnt();
            CommentLike commentLike = CommentLike.builder()
                    .comment(findComment)
                    .member(member)
                    .build();
            commentLikeRepository.save(commentLike);

            if (findComment.getMember() != member) { // ????????? ????????? ?????? ???????????? ???????????? ?????? ??????
                Optional<Notification> optionalNotification = notificationRepository.findByMemberIdAndCommentId(member.getId(), commentId);

                if (!optionalNotification.isPresent()) {
                    Notification notification = Notification.builder()
                            .comment(findComment)//?????? ?????????
                            .member(member)//???????????? ?????? ?????? ?????????
                            .ownerMember(findComment.getMember())//???????????? ??????
                            .build();

                    notificationRepository.save(notification);
                }
            }

        }

        return findComment.getLikeCnt();
    }

    @Override
    public CommentRetrieveResponseDto retrieveComment(Long commentId, String header) {
        Comment findComment = commentRepository.findIdWithFetchJoinMember(commentId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_COMMENT, "?????? ID??? ?????? ????????? ????????????.");
                });

        if (header == null) {
            return new CommentRetrieveResponseDto(findComment);
        } else {
            Member member;
            try {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(header);
                member = (Member)userDetailsService.loadUserByUsername(decodedToken.getUid());
            } catch (UsernameNotFoundException | FirebaseAuthException | IllegalArgumentException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "????????? ???????????? ????????? ???????????? ????????????.");
            }

            List<CommentLike> commentLikeList = findComment.getCommentLikeList();
            for (CommentLike commentLike : commentLikeList) {
                if (commentLike.getMember() == member)
                    return new CommentRetrieveLoginResponseDto(findComment, true);
            }
            return new CommentRetrieveLoginResponseDto(findComment, false);
        }
    }


    @Override
    public Page<CommentRetrieveResponseDto> retrieveAllComment(Long postId, String header, Pageable pageable) {

        if (header == null) {
            return commentRepository.findAllByIdWithFetchJoinMemberPaging(postId, pageable)
                    .map(comment -> new CommentRetrieveResponseDto(comment));
        } else {
            Member member;
            try {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(header);
                member = (Member)userDetailsService.loadUserByUsername(decodedToken.getUid());
            } catch (UsernameNotFoundException | FirebaseAuthException | IllegalArgumentException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "????????? ???????????? ????????? ???????????? ????????????.");
            }

            return commentRepository.findAllByIdWithFetchJoinMemberPaging(postId, pageable)
                    .map(comment -> {
                        List<CommentLike> commentLikeList = comment.getCommentLikeList();
                        for (CommentLike commentLike : commentLikeList) {
                            if (commentLike.getMember() == member)
                                return new CommentRetrieveLoginResponseDto(comment, true);
                        }
                        return new CommentRetrieveLoginResponseDto(comment, false);
                    });
        }
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, Member member) {

        Comment findComment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> {throw new CustomException(ErrorCode.NOT_FOUND_COMMENT, "?????? ID??? ?????? ????????? ????????????.");
                });

        if (findComment.getMember() != member){
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "?????? ????????? ????????? ????????????.");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public Page<MemberCommentsResponseDto> retrieveMemberComment(Member member, Pageable pageable) {
        Long memberId = member.getId();
        return commentRepository.findByMemberId(memberId, pageable)
                .map(comment -> new MemberCommentsResponseDto(comment));
    }

    @Override
    public long countMemberComments(Long id) {
        return commentRepository.countByMemberId(id);
    }
}
