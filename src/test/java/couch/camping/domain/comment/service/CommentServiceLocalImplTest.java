package couch.camping.domain.comment.service;

import couch.camping.controller.comment.dto.request.CommentEditRequestDto;
import couch.camping.controller.comment.dto.request.CommentWriteRequestDto;
import couch.camping.controller.comment.dto.response.CommentEditResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveResponseDto;
import couch.camping.controller.comment.dto.response.CommentWriteResponseDto;
import couch.camping.domain.comment.entity.Comment;
import couch.camping.domain.comment.repository.CommentRepository;
import couch.camping.domain.commentlike.entity.CommentLike;
import couch.camping.domain.commentlike.repository.CommentLikeRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.notification.repository.NotificationRepository;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.repository.PostRepository;
import couch.camping.domain.postimage.entity.PostImage;
import couch.camping.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommentServiceLocalImplTest {

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

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentLikeRepository commentLikeRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private CommentServiceLocalImpl commentServiceLocal;

    private Member member;
    private Post post;
    private List<PostImage> postImageList = new ArrayList<>();

    private Comment comment;
    private long commentID;
    private String commentContent;

    @BeforeEach
    void beforeEach() {
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
        }

        post = Post.builder()
                .id(postId)
                .member(member)
                .title(title)
                .content(content)
                .postType(postType)
                .postImageList(postImageList)
                .build();

        commentID = 3L;
        commentContent = "댓글";
        comment = Comment.builder()
                .id(commentID)
                .member(member)
                .post(post)
                .content(commentContent)
                .build();

    }

    @Test
    @DisplayName("댓글 작성 시 게시글이 없는 테스트")
    void writeCommentNotFoundPostTest() {

        //given
        when(postRepository.findById(any())).thenReturn(Optional.empty());

        //when
        //then
        assertThrows(CustomException.class,
                () -> commentServiceLocal.writeComment(new CommentWriteRequestDto(content), member, postId));
    }
    
    @Test
    @DisplayName("댓글 작성 테스트")
    public void writeCommentTest() throws Exception {
        //given
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        when(commentRepository.save(any())).thenReturn(comment);

        CommentWriteResponseDto expected = new CommentWriteResponseDto(comment);

        //when
        CommentWriteResponseDto actual =
                commentServiceLocal.writeComment(new CommentWriteRequestDto(content), member, postId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("댓글 작성 시 알림 테스트")
    public void writeCommentNotificationTest() throws Exception {
        //given
        post.setMember(new Member());
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        when(commentRepository.save(any())).thenReturn(comment);

        //when
        commentServiceLocal.writeComment(new CommentWriteRequestDto(content), member, postId);

        //then
        verify(notificationRepository, atLeastOnce()).save(any());

        post.setMember(member);
    }
    
    @Test
    @DisplayName("댓글 수정 테스트")
    public void editCommentNotFoundCommentTest() throws Exception {
        //given
        CommentEditRequestDto requestDto = new CommentEditRequestDto("수정 컨텐트");
        when(commentRepository.findById(any())).thenReturn(Optional.empty());

        //when
        //then
        assertThrows(CustomException.class, () -> commentServiceLocal.editComment(requestDto, member, commentID));
    }
    
    @Test
    @DisplayName("댓글 수정 성공 테스트")
    public void NoAuthorizationEditComment() throws Exception {
        //given
        comment.setMember(new Member());

        CommentEditRequestDto requestDto = new CommentEditRequestDto("수정 컨텐트");
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));

        //when
        //then
        assertThrows(CustomException.class, () -> commentServiceLocal.editComment(requestDto, member, commentID));

        comment.setMember(member);
    }
    
    @Test
    @DisplayName("댓글 수정 테스트")
    public void editCommentTest() throws Exception {
        //given
        String editContent = "수정 컨텐트";
        CommentEditRequestDto requestDto = new CommentEditRequestDto(editContent);
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));

        CommentEditResponseDto expected = new CommentEditResponseDto(commentID, editContent);

        //when
        CommentEditResponseDto actual = commentServiceLocal.editComment(requestDto, member, commentID);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("없는 댓글 좋아요 테스트")
    public void likeCommentNotFoundComment() throws Exception {
        //given
        when(commentRepository.findById(any())).thenReturn(Optional.empty());

        //when
        //then
        assertThrows(CustomException.class, () -> commentServiceLocal.likeComment(commentID, member));
    }

    @Test
    @DisplayName("좋아요한 적 없는 댓글 좋아요 시 좋아요 수 증가 테스트")
    public void alreadyLikedComment() throws Exception {
        //given
        comment.setLikeCnt(10);

        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        when(commentLikeRepository.findByMemberIdAndCommentId(any(), any())).thenReturn(Optional.ofNullable(new CommentLike()));

        //when
        int actual = commentServiceLocal.likeComment(commentID, member);

        //then
        assertThat(actual).isSameAs(9);
    }

    @Test
    @DisplayName("좋아요한 적 없는 댓글 좋아요 시 좋아요 수 증가 테스트")
    public void neverLikedComment() throws Exception {
        //given
        comment.setLikeCnt(10);

        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        when(commentLikeRepository.findByMemberIdAndCommentId(any(), any())).thenReturn(Optional.empty());

        //when
        int actual = commentServiceLocal.likeComment(commentID, member);

        //then
        assertThat(actual).isSameAs(11);
    }


    @Test
    @DisplayName("댓글 좋아요 시 알림 발생 테스트")
    public void neverLikedCommentNotificationTest() throws Exception {
        //given
        comment.setMember(new Member());
        comment.setLikeCnt(10);

        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        when(commentLikeRepository.findByMemberIdAndCommentId(any(), any())).thenReturn(Optional.empty());

        //when
        commentServiceLocal.likeComment(commentID, member);

        //then
        verify(notificationRepository, atLeastOnce()).save(any());

        comment.setMember(member);
    }

    @Test
    @DisplayName("비로그인 게시글 전체 조회")
    public void noLoginWithRetrieveAllComment() throws Exception {
        //given
        int size = 10;

        List<Comment> commentList = createCommentList(size);
        PageImpl<Comment> comments = new PageImpl<>(commentList, PageRequest.of(0, size), size);
        when(commentRepository.findAllByIdWithFetchJoinMemberPaging(any(), any())).thenReturn(comments);

        List<CommentRetrieveResponseDto> expected = commentMapToCommentRetrieveResponseDto(commentList);

        //when
        Page<CommentRetrieveResponseDto> actual = commentServiceLocal.retrieveAllComment(any(), null, any());

        //then
        assertThat(actual.getContent()).isEqualTo(expected);
    }

    @Test
    @DisplayName("로그인 게시글 전체 조회")
    public void loginWithRetrieveAllComment() throws Exception {
        //given
        int size = 10;

        List<Comment> commentList = createCommentList(size);
        PageRequest pageRequest = PageRequest.of(0, size);
        PageImpl<Comment> comments = new PageImpl<>(commentList, pageRequest, size);
        when(commentRepository.findAllByIdWithFetchJoinMemberPaging(any(), any())).thenReturn(comments);

        List<CommentRetrieveResponseDto> expected = commentMapToCommentRetrieveResponseDto(commentList);

        //when
        Page<CommentRetrieveResponseDto> actual = commentServiceLocal.retrieveAllComment(postId, "not null", pageRequest);

        //then
        actual.getContent().stream().forEach(System.out::println);
    }

    private List<CommentRetrieveResponseDto> commentMapToCommentRetrieveResponseDto(List<Comment> commentList) {
        return commentList.stream().map(c -> new CommentRetrieveResponseDto(c))
                .collect(Collectors.toList());
    }

    private List<Comment> createCommentList(int size) {
        List<Comment> commentList = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            Comment comment = Comment.builder()
                    .id(Long.valueOf(i))
                    .member(member)
                    .post(post)
                    .content("content" + i)
                    .build();
            commentList.add(comment);
        }
        return commentList;
    }

}