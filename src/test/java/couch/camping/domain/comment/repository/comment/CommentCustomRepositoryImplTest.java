package couch.camping.domain.comment.repository.comment;

import couch.camping.common.BaseControllerTest;
import couch.camping.domain.comment.entity.Comment;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.repository.MemberRepository;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CommentCustomRepositoryImplTest extends BaseControllerTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    final String uid = "1234";
    final String email = "email.@naver.com";
    final String name = "김상운";
    final String nickname = "balladang";

    final String postContent = "게시글 내용";
    final String commentContent = "댓글 내용";

    @Test
    @DisplayName("게시글의 댓글 조회")
    void findAllByPostIdWithFetchJoinMemberPaging() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);

        Member saveMember = memberRepository.save(createMember(uid, email, name, nickname));
        Post savePost = postRepository.save(createPost(saveMember, postContent, "picture", LocalDateTime.now()));

        Comment saveComment1 = commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));
        Comment saveComment2 = commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));
        Comment saveComment3 = commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));

        List<Comment> commentList = Arrays.asList(saveComment1, saveComment2, saveComment3);
        PageImpl<Comment> expected = new PageImpl<>(commentList, pageRequest, 3L);

        //when
        Page<Comment> result = commentRepository.findAllByPostIdWithFetchJoinMemberPaging(savePost.getId(), pageRequest);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("댓글 ID 로 댓글 조회")
    void findByIdWithFetchJoinMember() {
        //given
        Member saveMember = memberRepository.save(createMember(uid, email, name, nickname));
        Post savePost = postRepository.save(createPost(saveMember, postContent, "picture", LocalDateTime.now()));

        Comment saveComment1 = commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));
        Comment saveComment2 = commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));
        Comment saveComment3 = commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));


        //when
        Optional<Comment> findComment1 = commentRepository.findByIdWithFetchJoinMember(saveComment1.getId());
        Optional<Comment> findComment2 = commentRepository.findByIdWithFetchJoinMember(saveComment2.getId());
        Optional<Comment> findComment3 = commentRepository.findByIdWithFetchJoinMember(saveComment3.getId());

        //then
        assertThat(saveComment1).isEqualTo(findComment1.get());
        assertThat(saveComment2).isEqualTo(findComment2.get());
        assertThat(saveComment3).isEqualTo(findComment3.get());
    }

    @Test
    @DisplayName("회원의 댓글 조회")
    void findByMemberId() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);

        Member saveMember = memberRepository.save(createMember(uid, email, name, nickname));
        Post savePost = postRepository.save(createPost(saveMember, postContent, "picture", LocalDateTime.now()));

        Comment saveComment1 = commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));
        Comment saveComment2 = commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));
        Comment saveComment3 = commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));

        List<Comment> commentList = Arrays.asList(saveComment3, saveComment2, saveComment1);
        PageImpl<Comment> expected = new PageImpl<>(commentList, pageRequest, 3L);

        //when
        Page<Comment> postPage = commentRepository.findByMemberId(saveMember.getId(), pageRequest);

        //then
        assertThat(postPage).isEqualTo(expected);
        assertThat(postPage.getContent().get(1)).isEqualTo(saveComment2);
    }

    @Test
    void countByMemberId() {
        //given
        Member saveMember = memberRepository.save(createMember(uid, email, name, nickname));
        Post savePost = postRepository.save(createPost(saveMember, postContent, "picture", LocalDateTime.now()));

        commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));
        commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));
        commentRepository.save(createComment(saveMember, savePost, commentContent, LocalDateTime.now()));

        //when
        Long result = commentRepository.countByMemberId(saveMember.getId());

        //then
        assertThat(result).isEqualTo(3L);
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