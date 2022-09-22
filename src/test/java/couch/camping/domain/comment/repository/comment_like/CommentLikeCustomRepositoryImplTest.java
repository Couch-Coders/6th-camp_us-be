package couch.camping.domain.comment.repository.comment_like;

import couch.camping.common.BaseControllerTest;
import couch.camping.domain.comment.entity.Comment;
import couch.camping.domain.comment.entity.CommentLike;
import couch.camping.domain.comment.repository.comment.CommentRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.repository.MemberRepository;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CommentLikeCustomRepositoryImplTest extends BaseControllerTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentLikeRepository commentLikeRepository;

    final String uid = "1234";
    final String email = "email.@naver.com";
    final String name = "김상운";
    final String nickname = "balladang";

    final String postContent = "게시글 내용";
    final String commentContent = "댓글 내용";


    @Test
    @DisplayName("댓글의 회원이 좋아요한 댓글 좋아요 조회")
    public void findByMemberIdAndCommentId() throws Exception {
        //given
        Member saveMember = memberRepository.save(createMember(uid, email, name, nickname));
        Post savePost = postRepository.save(createPost(saveMember, postContent, "picture", LocalDateTime.now()));

        Comment saveComment = commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));

        CommentLike saveCommentLike = commentLikeRepository.save(createCommentLike(saveMember, saveComment));

        //when
        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByMemberIdAndCommentId(saveMember.getId(), saveComment.getId());

        //then
        assertThat(optionalCommentLike.get()).isEqualTo(saveCommentLike);
    }

    private CommentLike createCommentLike(Member saveMember, Comment saveComment) {
        return CommentLike.builder()
                .comment(saveComment)
                .member(saveMember)
                .build();
    }

    private Comment createComment(Member member, Post post, String commentContent, LocalDateTime createdDate) {
        return Comment.builder()
                .member(member)
                .post(post)
                .content(commentContent)
                .createdDate(createdDate)
                .build();
    }

    private Post createPost(Member member, String content, String postType, LocalDateTime createdDate) {
        return Post.builder()
                .member(member)
                .content(content)
                .postType(postType)
                .createdDate(createdDate)
                .build();
    }

    private Member createMember(String uid, String email, String name, String nickname) {
        return Member
                .builder()
                .uid(uid)
                .email(email)
                .name(name)
                .nickname(nickname)
                .build();
    }

}