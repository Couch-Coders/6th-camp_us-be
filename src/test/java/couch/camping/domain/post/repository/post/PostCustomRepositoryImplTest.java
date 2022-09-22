package couch.camping.domain.post.repository.post;

import couch.camping.common.BaseControllerTest;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.repository.MemberRepository;
import couch.camping.domain.post.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class PostCustomRepositoryImplTest extends BaseControllerTest {

    @Autowired
    EntityManager em;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;

    final String uid = "1234";
    final String email = "email.@naver.com";
    final String name = "김상운";
    final String nickname = "balladang";

    final String content = "게시글 내용";
    
    @Test
    @DisplayName("게시글 종류별 조회")
    void findAllByIdWithFetchJoinMemberPaging() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        
        List<Post> postList = new ArrayList<>();
        
        Member member = createMember(uid, email, name, nickname);
        memberRepository.save(member);

        Post post1 = createPost(member, content, "picture", LocalDateTime.now());
        Post post2 = createPost(member, content, "picture", LocalDateTime.now());
        Post post3 = createPost(member, content, "picture", LocalDateTime.now());

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        postList.add(post3);
        postList.add(post2);
        postList.add(post1);

        PageImpl<Post> expected = new PageImpl<>(postList, pageRequest, 3L);

        //when
        Page<Post> actual = postRepository.findAllByIdWithFetchJoinMemberPaging("all", pageRequest);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("게시글 좋아요 순 으로 조회")
    public void findAllBestPostTest() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Post> postList = new ArrayList<>();

        Member member = createMember(uid, email, name, nickname);
        memberRepository.save(member);

        Post post1 = createPost(member, content, "picture", LocalDateTime.now(), 4);
        Post post2 = createPost(member, content, "picture", LocalDateTime.now(), 7);
        Post post3 = createPost(member, content, "picture", LocalDateTime.now(), 2);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        postList.add(post2);
        postList.add(post1);
        postList.add(post3);

        PageImpl<Post> expected = new PageImpl<>(postList, pageRequest, 3L);

        //when
        Page<Post> actual = postRepository.findAllBestPost(pageRequest);

        //then
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("회원의 게시글 조회")
    public void findByMemberIdTest() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Post> postList = new ArrayList<>();

        Member member = createMember(uid, email, name, nickname);
        memberRepository.save(member);

        Post post1 = createPost(member, content, "picture", LocalDateTime.now());
        Post post2 = createPost(member, content, "picture", LocalDateTime.now());
        Post post3 = createPost(member, content, "picture", LocalDateTime.now());

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        postList.add(post3);
        postList.add(post2);
        postList.add(post1);

        PageImpl<Post> expected = new PageImpl<>(postList, pageRequest, 3L);

        //when
        Page<Post> actual = postRepository.findByMemberId(member.getId(), pageRequest);

        //then
        assertThat(actual).isEqualTo(expected);
    }
        
    @DisplayName("게시글 ID로 게시글 조회")
    @Test
    public void findByIdWithFetchJoinMemberTest() throws Exception {
        //given
        PersistenceUnitUtil unitUtil = em.getEntityManagerFactory().getPersistenceUnitUtil();
        Member member = createMember(uid, email, name, nickname);
        memberRepository.save(member);

        Post post = createPost(member, content, "picture", LocalDateTime.now());
        postRepository.save(post);

        em.flush();
        em.clear();

        Optional<Post> expected = Optional.ofNullable(post);

        //when
        Optional<Post> actual = postRepository.findByIdWithFetchJoinMember(post.getId());

        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(unitUtil.isLoaded(actual.get().getMember())).isTrue();
    }
        
    @DisplayName("회원이 작성항 게시글 수 조회")
    @Test
    public void countByMemberIdTest() throws Exception {
        //given
        Member member = createMember(uid, email, name, nickname);
        memberRepository.save(member);

        Post post1 = createPost(member, content, "picture", LocalDateTime.now());
        Post post2 = createPost(member, content, "picture", LocalDateTime.now());
        Post post3 = createPost(member, content, "picture", LocalDateTime.now());

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        Long expected = 3L;

        //when
        Long actual = postRepository.countByMemberId(member.getId());

        //then
        assertThat(actual).isEqualTo(expected);
    }
    
    private Post createPost(Member member, String content, String postType, LocalDateTime createdDate, int postLikeCnt) {
        return Post.builder()
                .member(member)
                .content(content)
                .postType(postType)
                .createdDate(createdDate)
                .likeCnt(postLikeCnt)
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