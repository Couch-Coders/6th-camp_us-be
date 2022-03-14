package couch.camping.domain.comment.service;

import com.google.api.gax.rpc.NotFoundException;
import couch.camping.controller.comment.dto.request.CommentEditRequestDto;
import couch.camping.controller.comment.dto.request.CommentWriteRequestDto;
import couch.camping.controller.comment.dto.response.CommentEditResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveAllResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveResponseDto;
import couch.camping.controller.comment.dto.response.CommentWriteResponseDto;
import couch.camping.domain.comment.entity.Comment;
import couch.camping.domain.comment.repository.CommentRepository;
import couch.camping.domain.commentlike.entity.CommentLike;
import couch.camping.domain.commentlike.repository.CommentLikeRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.repository.PostRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentWriteResponseDto writeComment(CommentWriteRequestDto commentWriteRequestDto, Member member, Long postId) {

        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> {throw new CustomException(ErrorCode.NOT_FOUND_POST, "게시글 ID 에 맞는 게시글이 없습니다.");
                });

        Comment comment = Comment.builder()
                .content(commentWriteRequestDto.getContent())
                .member(member)
                .post(post)
                .build();

        Comment saveComment = commentRepository.save(comment);

        return new CommentWriteResponseDto(saveComment);
    }

    @Transactional
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
        }
    }

    public CommentRetrieveResponseDto retrieveComment(Long commentId, Member member) {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_COMMENT, "댓글 ID에 맞는 댓글이 없습니다.");
                });

        return new CommentRetrieveResponseDto(findComment.getContent(), findComment.getId(), findComment.getMember().getId(), findComment.getLikeCnt());
    }


    public Page<CommentRetrieveAllResponseDto> retrieveAllComment(Long postId, Pageable pageable) {

        return commentRepository.findAllById(postId, pageable)
                .map(comment -> new CommentRetrieveAllResponseDto(comment));
    }
}
