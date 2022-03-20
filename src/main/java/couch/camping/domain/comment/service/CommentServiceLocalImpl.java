package couch.camping.domain.comment.service;

import couch.camping.controller.comment.dto.request.CommentEditRequestDto;
import couch.camping.controller.comment.dto.request.CommentWriteRequestDto;
import couch.camping.controller.comment.dto.response.CommentEditResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveLoginResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveResponseDto;
import couch.camping.controller.comment.dto.response.CommentWriteResponseDto;
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

import java.util.List;
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

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "게시글 ID 에 맞는 게시글이 없습니다.");
                });

        Comment comment = Comment.builder()
                .content(commentWriteRequestDto.getContent())
                .member(member)
                .post(findPost)
                .build();

        Comment saveComment = commentRepository.save(comment);

        if (findPost.getMember() != member) { // 자신의 게시글이 아닌 게시글에 댓글을 다는 경우

            Notification notification = Notification.builder()
                    .writeComment(saveComment)
                    .member(member)//좋아요를 누른 회원 엔티티
                    .ownerMember(findPost.getMember())//게시글의 회원
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
                .orElseThrow(() -> {throw new CustomException(ErrorCode.NOT_FOUND_COMMENT, "댓글 ID에 맞는 댓글이 없습니다.");
                });

        if (findComment.getMember() != member){
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 댓글이 아닙니다.");
        }

        findComment.editComment(commentEditRequestDto.getContent());

        return new CommentEditResponseDto(findComment);
    }

    @Transactional
    @Override
    public void likeComment(Long commentId, Member member) {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_COMMENT, "댓글 ID에 맞는 댓글이 없습니다.");
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

            if (findComment.getMember() != member) { // 자신의 댓글이 아닌 게시글을 좋아요를 누를 경우
                Optional<Notification> optionalNotification = notificationRepository.findByMemberIdAndCommentId(member.getId(), commentId);

                if (!optionalNotification.isPresent()) {
                    Notification notification = Notification.builder()
                            .comment(findComment)//댓글 엔티티
                            .member(member)//좋아요를 누른 회원 엔티티
                            .ownerMember(findComment.getMember())//게시글의 회원
                            .build();

                    notificationRepository.save(notification);
                }
            }

        }
    }

    @Override
    public CommentRetrieveResponseDto retrieveComment(Long commentId, String header) {
        Comment findComment = commentRepository.findIdWithFetchJoinMember(commentId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_COMMENT, "댓글 ID에 맞는 댓글이 없습니다.");
                });

        if (header == null) {
            return new CommentRetrieveResponseDto(findComment);
        } else {
            Member member;
            try {
                member = (Member)userDetailsService.loadUserByUsername(header);
            } catch (UsernameNotFoundException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "토큰에 해당하는 회원이 존재하지 않습니다.");
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
                member = (Member)userDetailsService.loadUserByUsername(header);
            } catch (UsernameNotFoundException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "토큰에 해당하는 회원이 존재하지 않습니다.");
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
                .orElseThrow(() -> {throw new CustomException(ErrorCode.NOT_FOUND_COMMENT, "댓글 ID에 맞는 댓글이 없습니다.");
                });

        if (findComment.getMember() != member){
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 댓글이 아닙니다.");
        }

        commentRepository.deleteById(commentId);
    }
}
