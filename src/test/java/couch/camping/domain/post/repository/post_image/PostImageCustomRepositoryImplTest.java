package couch.camping.domain.post.repository.post_image;

import couch.camping.common.BaseControllerTest;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.repository.MemberRepository;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.entity.PostImage;
import couch.camping.domain.post.repository.post.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

class PostImageCustomRepositoryImplTest extends BaseControllerTest {

    @Autowired
    PostImageRepository postImageRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;

    final String uid = "1234";
    final String email = "email.@naver.com";
    final String name = "김상운";
    final String nickname = "balladang";

    final String content = "게시글 내용";

    @Test
    @DisplayName("게시글 ID 로 게시글 이미지 삭제")
    public void deleteByPostIdTest() throws Exception {
        //given
        Member saveMember = memberRepository.save(createMember(uid, email, name, nickname));

        Post savePost = postRepository.save(createPost(saveMember, content, "picture", LocalDateTime.now()));

        postImageRepository.save(createPostImage(saveMember, savePost, "img"));
        postImageRepository.save(createPostImage(saveMember, savePost, "img"));
        postImageRepository.save(createPostImage(saveMember, savePost, "img"));

        //when
        long count = postImageRepository.deleteByPostId(savePost.getId());

        //then
        Assertions.assertThat(count).isEqualTo(3L);
    }

    private PostImage createPostImage(Member member, Post post, String img) {
        return PostImage.builder()
                .member(member)
                .post(post)
                .imgUrl(img)
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