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
        List<String> postTypeList = Arrays.asList("free", "picture", "question");

        validatePostType(postTypeList, postWriteRequestDto.getPostType());

        Post savePost = postRepository.save(addPostImageToPost(postWriteRequestDto, member, createPost(postWriteRequestDto, member)));

        return new PostWriteResponseDto(savePost, savePost.getPostImageList());
    }

    private Post addPostImageToPost(PostWriteRequestDto postWriteRequestDto, Member member, Post post) {
        for (String imgUrl : postWriteRequestDto.getImgUrlList()) {
            PostImage postImage = new PostImage(member, post, imgUrl);
            post.getPostImageList().add(postImage);
        }

        return post;
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
        Post post = Post.builder()
                .title(postWriteRequestDto.getTitle())
                .content(postWriteRequestDto.getContent())
                .postType(postWriteRequestDto.getPostType())
                .lastModifiedDate(LocalDateTime.now())
                .member(member)
                .build();
        return post;
    }

    @Transactional
    @Override
    public PostEditResponseDto editPost(Long postId, Member member, PostEditRequestDto postEditRequestDto) {
        Post findPost = findPostOrElseThrow(postRepository.findById(postId));

        validateAuthority(isSameMember(member, findPost), ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 게시글이 아닙니다.");

        findPost.editPost(postEditRequestDto.getTitle(), postEditRequestDto.getContent(), postEditRequestDto.getPostType());

        //벌크 연산, 영속성 컨텍스트 초기화
        postImageRepository.deleteByPostId(postId);

        List<PostImage> postImageList = createPostImageList(member, postEditRequestDto, findPost);
        postImageRepository.saveAll(postImageList);

        return new PostEditResponseDto(findPost, postImageList);

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

    private void validateAuthority(boolean isMyPost, ErrorCode errorCode, String message) {
        if (isMyPost) {
            throw new CustomException(errorCode, message);
        }
    }

    private Post findPostOrElseThrow(Optional<Post> optionalPost) {
        return optionalPost
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "게시글 ID 에 맞는 게시글이 없습니다.");
                });
    }

    @Transactional
    @Override
    public int likePost(Long postId, Member member) {
        Post findPost = findPostOrElseThrow(postRepository.findById(postId));

        Optional<PostLike> optionalPostLike = postLikeRepository.findByMemberIdAndPostId(member.getId(), postId);

        if (optionalPostLike.isPresent()) {//좋아요를 눌렀을 때
            alreadyLikedPost(findPost, optionalPostLike.get());
        } else {//좋아요를 누르지 않았을 때
            neverLikedPost(member, findPost);
            if (isSameMember(member, findPost)) { // 자신의 게시글이 아닌 게시글을 좋아요를 누를 경우
                Optional<Notification> optionalNotification = notificationRepository.findByMemberIdAndPostId(member.getId(), postId);
                if (!optionalNotification.isPresent()) {
                    notificationRepository.save(createNotification(member, findPost));
                }
            }
        }

        return findPost.getLikeCnt();
    }

    private Notification createNotification(Member member, Post post) {
        Notification notification = Notification.builder()
                .post(post)//게시글 엔티티
                .member(member)//좋아요를 누른 회원 엔티티
                .ownerMember(post.getMember())//게시글의 회원
                .build();
        return notification;
    }

    private boolean isSameMember(Member member, Post post) {
        return post.getMember() != member;
    }

    private void neverLikedPost(Member member, Post post) {
        post.increaseLikeCnt();
        PostLike postLike = PostLike.builder()
                .post(post)
                .member(member)
                .build();

        postLikeRepository.save(postLike);
    }

    private void alreadyLikedPost(Post post, PostLike postLike) {
        post.decreaseLikeCnt();
        postLikeRepository.deleteById(postLike.getId());
    }

    @Override
    public PostRetrieveResponseDto retrievePost(Long postId, String header) {
        Post findPost = findPostOrElseThrow(postRepository.findByIdWithFetchJoinMember(postId));

        if (header == null) {//비로그인
            return new PostRetrieveResponseDto(findPost, findPost.getCommentList().size(), findPost.getPostImageList());
        } else {//로그인
            Member member = getMemberOrElseThrow(header);
            Optional<PostLike> optionalPostLike = postLikeRepository.findByMemberIdAndPostId(member.getId(), postId);
            return new PostRetrieveLoginResponseDto(findPost, findPost.getCommentList().size(), findPost.getPostImageList(), optionalPostLike.isPresent());
        }
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

    @Override
    public Page<PostRetrieveResponseDto> retrieveAllPost(String postType, Pageable pageable, String header) {
        List<String> postTypeList = Arrays.asList("all", "free", "picture", "question");
        validatePostType(postTypeList, postType);

        if (header == null) {
            return findAllPostPageAndMapToPostRetrieveResponseDto(postType, pageable);
        } else {
            Member member = getMemberOrElseThrow(header);
            return findAllPostPageAndMapToPostRetrieveLoginResponseDto(member, postRepository.findAllByIdWithFetchJoinMemberPaging(postType, pageable));
        }
    }

    private Page<PostRetrieveResponseDto> findAllPostPageAndMapToPostRetrieveLoginResponseDto(Member member, Page<Post> postPage) {
        return postPage
                .map(post -> {
                    List<PostLike> postLikeList = post.getPostLikeList();
                    for (PostLike postLike : postLikeList) {
                        if (postLike.getMember() == member) {
                            return new PostRetrieveLoginResponseDto(post, post.getCommentList().size(), post.getPostImageList(), true);
                        }
                    }
                    return new PostRetrieveLoginResponseDto(post, post.getCommentList().size(), post.getPostImageList(), false);
                });
    }

    private Page<PostRetrieveResponseDto> findAllPostPageAndMapToPostRetrieveResponseDto(String postType, Pageable pageable) {
        return postRepository.findAllByIdWithFetchJoinMemberPaging(postType, pageable)
                .map(post -> new PostRetrieveResponseDto(post, post.getCommentCnt(), post.getPostImageList()));
    }

    @Override
    public Page<PostRetrieveResponseDto> retrieveAllBestPost(Pageable pageable, String header) {
        if (header == null)
            return findAllBestPostPageAndMapToPostRetrieveResponseDto(pageable);
        else {
            Member member = getMemberOrElseThrow(header);

            return findAllPostPageAndMapToPostRetrieveLoginResponseDto(member, postRepository.findAllBestPost(pageable));
        }
    }

    private Page<PostRetrieveResponseDto> findAllBestPostPageAndMapToPostRetrieveResponseDto(Pageable pageable) {
        return postRepository.findAllBestPost(pageable)
                .map(post -> new PostRetrieveResponseDto(post, post.getCommentList().size(), post.getPostImageList()));
    }

    @Transactional
    @Override
    public void deletePost(Long postId, Member member) {
        Post findPost = findPostOrElseThrow(postRepository.findById(postId));

        validateAuthority(isSameMember(member, findPost), ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 게시글이 아닙니다.");

        postRepository.deleteById(postId);
    }

    @Override
    public Page<MemberPostResponseDto> retrieveMemberComment(Member member, Pageable pageable) {
        return postRepository.findByMemberId(member.getId(), pageable)
                .map(post -> new MemberPostResponseDto(post));
    }

    @Override
    public long countMemberPosts(Long id) {
        return postRepository.countByMemberId(id);
    }
}
