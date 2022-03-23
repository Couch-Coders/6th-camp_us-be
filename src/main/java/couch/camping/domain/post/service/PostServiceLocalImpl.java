package couch.camping.domain.post.service;

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
import couch.camping.domain.post.repository.PostRepository;
import couch.camping.domain.postimage.entity.PostImage;
import couch.camping.domain.postimage.repository.PostImageRepository;
import couch.camping.domain.postlike.entity.PostLike;
import couch.camping.domain.postlike.repository.PostLikeRepository;
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

@Profile("local")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceLocalImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserDetailsService userDetailsService;
    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public PostWriteResponseDto writePost(PostWriteRequestDto postWriteRequestDto, Member member) {
        List<String> postTypeList = Arrays.asList("free", "picture", "question");

        if (!postTypeList.contains(postWriteRequestDto.getPostType()))
            throw new CustomException(ErrorCode.BAD_REQUEST_PARAM, "postType 의 값은 free, picture, question 만 가능합니다.");

        Post post = Post.builder()
                .title(postWriteRequestDto.getTitle())
                .content(postWriteRequestDto.getContent())
                .postType(postWriteRequestDto.getPostType())
                .lastModifiedDate(LocalDateTime.now())
                .member(member)
                .build();

        for (String imgUrl : postWriteRequestDto.getImgUrlList()) {
            PostImage postImage = new PostImage(member, post, imgUrl);
            post.getPostImageList().add(postImage);
        }

        Post savePost = postRepository.save(post);

        return new PostWriteResponseDto(savePost, savePost.getPostImageList());
    }

    @Transactional
    @Override
    public PostEditResponseDto editPost(Long postId, Member member, PostEditRequestDto postEditRequestDto) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "게시글 ID 에 맞는 게시글이 없습니다.");
                });

        if (findPost.getMember() != member) {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 게시글이 아닙니다.");
        }

        findPost.editPost(postEditRequestDto.getTitle(), postEditRequestDto.getContent(), postEditRequestDto.getPostType());

        //벌크 연산, 영속성 컨텍스트 초기화
        postImageRepository.deleteByPostId(postId);

        List<PostImage> postImageList = new ArrayList<>();
        for (String imgUrl : postEditRequestDto.getImgUrlList()) {
            PostImage postImage = PostImage.builder()
                    .post(findPost)
                    .member(member)
                    .imgUrl(imgUrl)
                    .build();
            postImageList.add(postImage);
        }
        postImageRepository.saveAll(postImageList);

        return new PostEditResponseDto(findPost, postImageList);

    }

    @Transactional
    @Override
    public void likePost(Long postId, Member member) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "게시글 ID 에 맞는 게시글이 없습니다.");
                });

        Optional<PostLike> optionalPostLike = postLikeRepository.findByMemberIdAndPostId(member.getId(), postId);

        if (optionalPostLike.isPresent()) {//좋아요를 눌렀을 때
            findPost.decreaseLikeCnt();
            postLikeRepository.deleteById(optionalPostLike.get().getId());
        } else {//좋아요를 누르지 않았을 때
            findPost.increaseLikeCnt();
            PostLike postLike = PostLike.builder()
                    .post(findPost)
                    .member(member)
                    .build();

            postLikeRepository.save(postLike);

            if (findPost.getMember() != member) { // 자신의 게시글이 아닌 게시글을 좋아요를 누를 경우
                Optional<Notification> optionalNotification = notificationRepository.findByMemberIdAndPostId(member.getId(), postId);

                if (!optionalNotification.isPresent()) {
                    Notification notification = Notification.builder()
                            .post(findPost)//게시글 엔티티
                            .member(member)//좋아요를 누른 회원 엔티티
                            .ownerMember(findPost.getMember())//게시글의 회원
                            .build();

                    notificationRepository.save(notification);
                }
            }

        }
    }

    @Override
    public PostRetrieveResponseDto retrievePost(Long postId, String header) {
        Post findPost = postRepository.findByIdWithFetchJoinMember(postId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "게시글 ID 에 맞는 게시글이 없습니다.");
                });

        if (header == null) {//비로그인
            return new PostRetrieveResponseDto(findPost, findPost.getCommentList().size(), findPost.getPostImageList());
        } else {//로그인
            Member member;
            try {
                member = (Member)userDetailsService.loadUserByUsername(header);
            } catch (UsernameNotFoundException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "토큰에 해당하는 회원이 존재하지 않습니다.");
            }
            Optional<PostLike> optionalPostLike = postLikeRepository.findByMemberIdAndPostId(member.getId(), postId);
            return new PostRetrieveLoginResponseDto(findPost, findPost.getCommentList().size(), findPost.getPostImageList(), optionalPostLike.isPresent());
        }
    }

    @Override
    public Page<PostRetrieveResponseDto> retrieveAllPost(String postType, Pageable pageable, String header) {
        List<String> postTypeList = Arrays.asList("all", "free", "picture", "question");

        if (!postTypeList.contains(postType)) {
            throw new CustomException(ErrorCode.BAD_REQUEST_PARAM, "쿼리스트링 postType 의 값은 all, free, picture, question 만 가능합니다.");
        }
        if (header == null) {
            return postRepository.findAllByIdWithFetchJoinMemberPaging(postType, pageable)
                    .map(post -> new PostRetrieveResponseDto(post, post.getCommentList().size(), post.getPostImageList()));
        } else {
            Member member;
            try {
                member = (Member)userDetailsService.loadUserByUsername(header);
            } catch (UsernameNotFoundException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "토큰에 해당하는 회원이 존재하지 않습니다.");
            }

            return postRepository.findAllByIdWithFetchJoinMemberPaging(postType, pageable)
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
    }

    @Override
    public Page<PostRetrieveResponseDto> retrieveAllBestPost(Pageable pageable) {
        return postRepository.findAllBestPost(pageable)
                .map(post -> new PostRetrieveResponseDto(post, post.getCommentList().size(), post.getPostImageList()));
    }

    @Transactional
    @Override
    public void deletePost(Long postId, Member member) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "게시글 ID 에 맞는 게시글이 없습니다.");
                });
        
        if (findPost.getMember() != member) {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 게시글이 아닙니다.");
        }

        postRepository.deleteById(postId);
    }

    @Override
    public Page<MemberPostResponseDto> retrieveMemberComment(Member member, Pageable pageable) {
        Long memberId = member.getId();
        return postRepository.findByMemberId(memberId, pageable)
                .map(post -> new MemberPostResponseDto(post));
    }
}
