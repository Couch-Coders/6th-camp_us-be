package couch.camping.domain.post.service;

import couch.camping.controller.post.dto.request.PostEditRequestDto;
import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.controller.post.dto.response.PostEditResponseDto;
import couch.camping.controller.post.dto.response.PostRetrieveLoginResponseDto;
import couch.camping.controller.post.dto.response.PostRetrieveResponseDto;
import couch.camping.controller.post.dto.response.PostWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.entity.PostImage;
import couch.camping.domain.post.entity.PostLike;
import couch.camping.domain.post.repository.post.PostRepository;
import couch.camping.domain.post.repository.post_like.PostLikeRepository;
import couch.camping.domain.post.service.post.impl.PostServiceLocalImpl;
import couch.camping.exception.CustomException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PostServiceLocalImplTest {
    
    //member 필드
    private static Long memberId = 1L;
    private static final String uid = "abcd";
    private static final String email = "abcd@daum.com";
    private static final String name = "성이름";
    private static final String nickname = "abcd";
    private static final String imgUrl = "https://www.balladang.com";
    
    //post 필드
    private static Long postId = 2L;
    private static String title = "캠핑장 같이 가실 분~?";
    private static String content = "장비는 제가 준비 하겠습니다.";
    private static String postType = "free";
    private static List<String> imgUrlList = Arrays.asList("picture1", "picture2", "picture3");

    //postLike 필드
    private static Long postLikeId = 2L;


    @Mock
    PostRepository postRepository;
    @Mock
    PostLikeRepository postLikeRepository;
    @Mock
    UserDetailsService userDetailsService;

    @InjectMocks
    PostServiceLocalImpl postServiceLocal;

    Member member;

    Post post;

    PostLike postLike;

    List<PostImage> postImageList = new ArrayList<>();

    @BeforeEach
    void before() {
        member = Member.builder()
                .id(memberId)
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
                    .id(postId)
                    .member(member)
                    .title(title)
                    .content(content)
                    .postType(postType)
                    .postImageList(postImageList)
                    .build();
        }

        postLike = PostLike.builder()
                .id(postLikeId)
                .post(post)
                .member(member)
                .build();
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
                .postId(postId)
                .title(title)
                .content(content)
                .postType(postType)
                .imgUrlList(imgUrlList)
                .build();

        when(postRepository.save(any())).thenReturn(post);

        //when
        PostWriteResponseDto writeResponseDto = postServiceLocal.writePost(postWriteRequestDto, member);

        //then
        assertThat(writeResponseDto).isEqualTo(postWriteResponseDto);
    }
    
    @Test
    @DisplayName("게시글 수정 테스트")
    void editPostTest() {

        //given
        String editTitle = "수정 제목";
        String editContent = "수정 내용";
        String editPostType = "picture";
        List<String> editImgUrlList = Arrays.asList("www.picture.com");

        PostEditRequestDto postEditRequestDto = PostEditRequestDto.builder()
                .title(editTitle)
                .content(editContent)
                .postType(editPostType)
                .imgUrlList(editImgUrlList)
                .build();

        PostEditResponseDto postEditResponseDto = PostEditResponseDto.builder()
                .postId(postId)
                .title(editTitle)
                .content(editContent)
                .postType(editPostType)
                .imgUrlList(editImgUrlList)
                .build();

        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));

        //when
        PostEditResponseDto responseDto = postServiceLocal.editPost(postId, member, postEditRequestDto);

        //then
        assertThat(responseDto).isEqualTo(postEditResponseDto);
    }
    
    @Test
    @DisplayName("게시글 좋아요 테스트")
    void likePostTest() {

        //given
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));

        //when
        int likePost = postServiceLocal.likePost(postId, member);

        //then
        assertThat(likePost).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 중복 좋아요 테스트")
    void duplicateLikePostTest() {
        //given
        post.setLikeCnt(1);
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(postLikeRepository.findByMemberIdAndPostId(memberId, postId)).thenReturn(Optional.ofNullable(postLike));

        //when
        int likePost = postServiceLocal.likePost(postId, member);

        //then
        assertThat(likePost).isEqualTo(0);
    }

    @Test
    @DisplayName("없는 게시글 조회 테스트")
    public void RetrieveNotExistPostTest () throws Exception {
        //given
        when(postRepository.findByIdWithFetchJoinMember(any())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThrows(CustomException.class, () -> postServiceLocal.retrievePost(postId, null));
    }

    @Test
    @DisplayName("비로그인 게시글 조회 테스트")
    public void NoLoginWithRetrievePostTest () throws Exception {
        //given
        PostRetrieveResponseDto postRetrieveResponseDto = new PostRetrieveResponseDto(post, 0, postImageList);
        when(postRepository.findByIdWithFetchJoinMember(any())).thenReturn(Optional.ofNullable(post));

        //when
        PostRetrieveResponseDto actualResponseDto = postServiceLocal.retrievePost(postId, null);

        //then
        assertThat(actualResponseDto).isEqualTo(postRetrieveResponseDto);
    }

    @Test
    @DisplayName("로그인 게시글 조회 테스트")
    public void LoginWithRetrievePostTest () throws Exception {

        //given
        PostRetrieveLoginResponseDto postRetrieveLoginResponseDto = new PostRetrieveLoginResponseDto(post, 0, postImageList, true);
        when(postRepository.findByIdWithFetchJoinMember(any())).thenReturn(Optional.ofNullable(post));
        when(userDetailsService.loadUserByUsername(any())).thenReturn(member);
        when(postLikeRepository.findByMemberIdAndPostId(any(), any())).thenReturn(Optional.ofNullable(postLike));

        //when
        PostRetrieveLoginResponseDto actualResponseDto = (PostRetrieveLoginResponseDto)postServiceLocal.retrievePost(postId, "header");

        //then
        assertThat(actualResponseDto).isEqualTo(postRetrieveLoginResponseDto);
    }

    @Test
    @DisplayName("postType 이 예외인 게시글 전체 조회 테스트")
    public void retrieveAllPostThrowException() throws Exception {
        //given
        postType = "exception";
        Pageable pageable = PageRequest.of(0, 10);
        String header = "Bearer abcd";

        //when
        //then
        Assertions.assertThrows(CustomException.class, () -> {
            postServiceLocal.retrieveAllPost(postType, pageable, header);
        });

        postType = "free";
    }

    @Test
    @DisplayName("비로그인 후 게시글 전체 조회 테스트")
    public void NoLoginWithRetrieveAllPost() throws Exception {
        //given
        String header = null;
        int size = 10;

        List<Post> postList = createPost(size);
        Pageable pageable = PageRequest.of(1, 5);
        PageImpl<Post> postPage = new PageImpl<>(postList.subList(5, 10), pageable, size);

        when(postRepository.findAllByIdWithFetchJoinMemberPaging(any(), any()))
                .thenReturn(postPage);

        List<PostRetrieveResponseDto> expected = postMapToPostRetrieveResponseDto(postList);

        //when
        Page<PostRetrieveResponseDto> actual = postServiceLocal.retrieveAllPost(postType, pageable, header);

        //then
        assertThat(actual.getContent()).isEqualTo(expected.subList(5, 10));
    }

    private List<Post> createPost(int rep) {
        List<Post> postList = new ArrayList<>();
        for (int i = 1; i <= rep; i++) {
            Post post = Post.builder()
                    .id(Long.valueOf(i))
                    .member(member)
                    .title(title + i)
                    .content(content + i)
                    .postType(postType)
                    .commentCnt(5)
                    .build();
            post.setPostImageList(postImageList);

            postList.add(post);
        }
        return postList;
    }

    private List<PostRetrieveResponseDto> postMapToPostRetrieveResponseDto(List<Post> postList) {
        return postList.stream().map(p ->
                new PostRetrieveResponseDto(p, p.getCommentCnt(), p.getPostImageList())
        ).collect(Collectors.toList());
    }

    private List<PostRetrieveLoginResponseDto> postMapToPostRetrieveLoginResponseDto(List<Post> postList) {
        return postList.stream().map(p ->
                new PostRetrieveLoginResponseDto(p, p.getCommentCnt(), p.getPostImageList(), false)
        ).collect(Collectors.toList());
    }

}