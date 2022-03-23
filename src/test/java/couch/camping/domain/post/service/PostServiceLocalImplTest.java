package couch.camping.domain.post.service;

import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.controller.post.dto.response.PostWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.notification.repository.NotificationRepository;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.repository.PostRepository;
import couch.camping.domain.postimage.entity.PostImage;
import couch.camping.domain.postimage.repository.PostImageRepository;
import couch.camping.domain.postlike.repository.PostLikeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PostServiceLocalImplTest {
    
    //member 필드
    private static final String uid = "abcd";
    private static final String email = "abcd@daum.com";
    private static final String name = "성이름";
    private static final String nickname = "abcd";
    private static final String imgUrl = "https://www.balladang.com";
    
    //post 필드
    private static String title = "캠핑장 같이 가실 분~?";
    private static String content = "장비는 제가 준비 하겠습니다.";
    private static String postType = "free";
    private static List<String> imgUrlList = Arrays.asList("picture1", "picture2", "picture3");


    @Mock
    PostRepository postRepository;
    @Mock
    PostImageRepository postImageRepository;
    @Mock
    PostLikeRepository postLikeRepository;
    @Mock
    UserDetailsService userDetailsService;
    @Mock
    NotificationRepository notificationRepository;
    
    @InjectMocks
    PostServiceLocalImpl postServiceLocal;

    Post post;

    List<PostImage> postImageList = new ArrayList<>();;

    Member member;

    @BeforeEach
    void before() {
        member = Member.builder()
                .id(1L)
                .uid(uid)
                .email(email)
                .name(name)
                .nickname(nickname)
                .imgUrl(imgUrl)
                .build();

        for (String s : imgUrlList) {
            postImageList.add(
                    PostImage.builder()
                            .imgUrl(s).build()
            );

            post = Post.builder()
                    .id(1L)
                    .member(member)
                    .title(title)
                    .content(content)
                    .postType(postType)
                    .postImageList(postImageList)
                    .build();
        }
    }

    @AfterEach
    void after() {
        postImageList.clear();
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    void writePostTest() {
        //given
        PostWriteRequestDto postWriteRequestDto = PostWriteRequestDto.builder()
                .title(title)
                .content(content)
                .postType(postType)
                .imgUrlList(imgUrlList)
                .build();

        PostWriteResponseDto postWriteResponseDto = PostWriteResponseDto.builder()
                .postId(1L)
                .title(title)
                .content(content)
                .postType(postType)
                .imgUrlList(imgUrlList)
                .build();

        //when
        when(postRepository.save(any())).thenReturn(post);

        //then
        assertThat(postServiceLocal.writePost(postWriteRequestDto, member)).isEqualTo(postWriteResponseDto);
    }
    
}