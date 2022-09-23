package couch.camping.domain.post.service.post.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.member.dto.response.MemberPostResponseDto;
import couch.camping.controller.post.dto.request.PostEditRequestDto;
import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.controller.post.dto.response.PostEditResponseDto;
import couch.camping.controller.post.dto.response.PostRetrieveLoginResponseDto;
import couch.camping.controller.post.dto.response.PostRetrieveResponseDto;
import couch.camping.controller.post.dto.response.PostWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.notification.entity.Notification;
import couch.camping.domain.notification.repository.NotificationRepository;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.repository.post.PostRepository;
import couch.camping.domain.post.entity.PostImage;
import couch.camping.domain.post.repository.post_image.PostImageRepository;
import couch.camping.domain.post.entity.PostLike;
import couch.camping.domain.post.repository.post_like.PostLikeRepository;
import couch.camping.domain.post.service.post.PostService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Profile("prod")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserDetailsService userDetailsService;
    private final NotificationRepository notificationRepository;
    private final FirebaseAuth firebaseAuth;

    @Transactional
    @Override
    public PostWriteResponseDto writePost(PostWriteRequestDto postWriteRequestDto, Member member) {
        validatePostType(Arrays.asList("free", "picture", "question"), postWriteRequestDto.getPostType());
        Post savePost = savePost(postWriteRequestDto, member);
        return new PostWriteResponseDto(savePost, savePost.getPostImageList());
    }

    @Transactional
    @Override
    public PostEditResponseDto editPost(Long postId, Member member, PostEditRequestDto postEditRequestDto) {
        Post findPost = findPostOrElseThrow(postId);

        validateAuthority(member, findPost.getMember());

        findPost.editPost(postEditRequestDto.getTitle(), postEditRequestDto.getContent(), postEditRequestDto.getPostType());

        //벌크 연산, 영속성 컨텍스트 초기화
        deletePostImage(postId);

        List<PostImage> postImageList = createPostImageList(member, postEditRequestDto, findPost);
        savePostImageList(postImageList);
        return new PostEditResponseDto(findPost, postImageList);
    }

    @Transactional
    @Override
    public int likePost(Long postId, Member member) {
        Post findPost = findPostOrElseThrow(postId);

        Optional<PostLike> optionalPostLike = findMemberLikePost(postId, member);

        if (optionalPostLike.isPresent()) {//좋아요를 눌렀을 때
            decreasePostLikeCnt(findPost);
            deletePostLke(optionalPostLike);
        } else {//좋아요를 누르지 않았을 때
            increasePostLikeCnt(findPost);
            savePostLike(member, findPost);
            checkToSaveNotification(postId, member, findPost);
        }

        return findPost.getLikeCnt();
    }

    @Override
    public PostRetrieveResponseDto retrievePost(Long postId, String header) {
        Post findPost = findPostWithFetchJoinMember(postId);

        if (header == null) {//비로그인
            return createPostRetrieveResponseDto(findPost);
        } else {//로그인
            Member member = getMemberOrElseThrow(header);
            Optional<PostLike> optionalPostLike = findMemberLikePost(postId, member);
            return createPostRetrieveLoginResponseDto(findPost, optionalPostLike.isPresent());
        }

    }

    @Override
    public Page<PostRetrieveResponseDto> retrieveAllPost(String postType, Pageable pageable, String header) {
        List<String> postTypeList = Arrays.asList("all", "free", "picture", "question");
        validatePostType(postTypeList, postType);

        if (header == null)
            return findAllByIdWithFetchJoinMemberPaging(postType, pageable)
                    .map(post -> createRetrieveResponseDto(post));
        else {
            return findAllByIdWithFetchJoinMemberPaging(postType, pageable)
                    .map(post -> createLoginRetrieveResponseDto(getMemberOrElseThrow(header), post));
        }
    }

    @Override
    public Page<PostRetrieveResponseDto> retrieveAllBestPost(Pageable pageable, String header) {
        if (header == null)
            return findAllBestPagingPost(pageable)
                    .map(post -> createRetrieveResponseDto(post));
        else
            return findAllBestPagingPost(pageable)
                    .map(post -> createLoginRetrieveResponseDto(getMemberOrElseThrow(header), post));
    }

    @Transactional
    @Override
    public void deletePost(Long postId, Member member) {
        Post findPost = findPostOrElseThrow(postId);

        validateAuthority(member, findPost.getMember());

        deletePostByPostId(postId);
    }

    @Override
    public Page<MemberPostResponseDto> retrieveMemberComment(Member member, Pageable pageable) {
        return findPostByMemberId(member, pageable)
                .map(post -> new MemberPostResponseDto(post));
    }

    @Override
    public long countMemberPosts(Long id) {
        return countPostByMemberId(id);
    }

    private Long countPostByMemberId(Long id) {
        return postRepository.countByMemberId(id);
    }

    private Page<Post> findPostByMemberId(Member member, Pageable pageable) {
        return postRepository.findByMemberIdWithPaging(member.getId(), pageable);
    }

    private void deletePostByPostId(Long postId) {
        postRepository.deleteById(postId);
    }

    private Page<Post> findAllBestPagingPost(Pageable pageable) {
        return postRepository.findAllBestPost(pageable);
    }

    private Page<Post> findAllByIdWithFetchJoinMemberPaging(String postType, Pageable pageable) {
        return postRepository.findAllByIdWithFetchJoinMemberPaging(postType, pageable);
    }

    private PostRetrieveResponseDto createLoginRetrieveResponseDto(Member member, Post post) {
        for (PostLike postLike : post.getPostLikeList()) {
            if (!isNotSameMember(postLike.getMember(), member)) {
                return createPostRetrieveLoginResponseDto(post, true);
            }
        }
        return createPostRetrieveLoginResponseDto(post, false);
    }

    private PostRetrieveResponseDto createRetrieveResponseDto(Post post) {
        return new PostRetrieveResponseDto(post, post.getCommentCnt(), post.getPostImageList());
    }

    private Optional<PostLike> findMemberLikePost(Long postId, Member member) {
        return postLikeRepository.findByMemberIdAndPostId(member.getId(), postId);
    }

    private PostRetrieveResponseDto createPostRetrieveLoginResponseDto(Post findPost, boolean present) {
        return new PostRetrieveLoginResponseDto(findPost, findPost.getCommentList().size(), findPost.getPostImageList(), present);
    }

    private PostRetrieveResponseDto createPostRetrieveResponseDto(Post findPost) {
        return createRetrieveResponseDto(findPost);
    }

    private Post findPostWithFetchJoinMember(Long postId) {
        return postRepository.findByIdWithFetchJoinMember(postId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "게시글 ID 에 맞는 게시글이 없습니다.");
                });
    }

    private void checkToSaveNotification(Long postId, Member member, Post findPost) {
        if (isNotSameMember(member, findPost.getMember())) { // 자신의 게시글이 아닌 게시글을 좋아요를 누를 경우
            if (!isExistMemberLikePostNotification(postId, member)) {
                saveNotification(createNotification(member, findPost));
            }
        }
    }

    private boolean isExistMemberLikePostNotification(Long postId, Member member) {
        return notificationRepository.findByMemberIdAndPostId(member.getId(), postId).isPresent();
    }

    private void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    private void deletePostLke(Optional<PostLike> optionalPostLike) {
        postLikeRepository.deleteById(optionalPostLike.get().getId());
    }

    private void decreasePostLikeCnt(Post findPost) {
        findPost.decreaseLikeCnt();
    }

    private void increasePostLikeCnt(Post findPost) {
        findPost.increaseLikeCnt();
    }

    private PostLike savePostLike(Member member, Post findPost) {
        return postLikeRepository.save(createPostLike(member, findPost));
    }

    private PostLike createPostLike(Member member, Post findPost) {
        return PostLike.builder()
                .post(findPost)
                .member(member)
                .build();
    }

    private Notification createNotification(Member member, Post post) {
        return Notification.builder()
                .post(post)//게시글 엔티티
                .member(member)//좋아요를 누른 회원 엔티티
                .ownerMember(post.getMember())//게시글의 회원
                .build();
    }

    private boolean isNotSameMember(Member loginMember, Member ownerMember) {
        return loginMember != ownerMember;
    }

    private void deletePostImage(Long postId) {
        postImageRepository.deleteByPostId(postId);
    }

    private List<PostImage> createPostImageList(Member member, PostEditRequestDto postEditRequestDto, Post findPost) {
        List<PostImage> postImageList = new ArrayList<>();
        for (String imgUrl : postEditRequestDto.getImgUrlList()) {
            PostImage postImage = PostImage.builder()
                    .post(findPost)
                    .member(member)
                    .imgUrl(imgUrl)
                    .build();
            postImageList.add(postImage);
        }
        return postImageList;
    }

    private void validateAuthority(Member loginMember, Member ownerMember) {
        if (isNotSameMember(loginMember, ownerMember)) {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 게시글이 아닙니다.");
        }
    }

    private Post findPostOrElseThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "게시글 ID 에 맞는 게시글이 없습니다.");
                });
    }

    private Post savePost(PostWriteRequestDto postWriteRequestDto, Member member) {
        Post post = createPost(postWriteRequestDto, member);
        addPostImageToPost(post, postWriteRequestDto.getImgUrlList());
        return postRepository.save(post);
    }

    private void addPostImageToPost(Post post, List<String> imgUrlList) {
        for (String imgUrl : imgUrlList) {
            post.getPostImageList().add(new PostImage(post.getMember(), post, imgUrl));
        }
    }

    private void validatePostType(List<String> postTypeList, String postType) {

        StringBuilder sb = new StringBuilder();
        sb.append("쿼리스트링 postType 의 값은 ");
        for (String s : postTypeList) {
            sb.append(s + " ");
        }
        sb.append("만 가능합니다.");

        if (!postTypeList.contains(postType))
            throw new CustomException(ErrorCode.BAD_REQUEST_PARAM, sb.toString());
    }

    private Post createPost(PostWriteRequestDto postWriteRequestDto, Member member) {
        return Post.builder()
                .title(postWriteRequestDto.getTitle())
                .content(postWriteRequestDto.getContent())
                .postType(postWriteRequestDto.getPostType())
                .lastModifiedDate(LocalDateTime.now())
                .member(member)
                .build();
    }

    private void savePostImageList(List<PostImage> postImageList) {
        postImageRepository.saveAll(postImageList);
    }

    private Member getMemberOrElseThrow(String header) {
        Member member;
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(header);
            member = (Member)userDetailsService.loadUserByUsername(decodedToken.getUid());
        } catch (UsernameNotFoundException | FirebaseAuthException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "토큰에 해당하는 회원이 존재하지 않습니다.");
        }
        return member;
    }
}
