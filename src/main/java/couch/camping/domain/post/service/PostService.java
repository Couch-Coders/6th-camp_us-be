package couch.camping.domain.post.service;

import couch.camping.controller.post.dto.request.PostEditRequestDto;
import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.controller.post.dto.response.PostEditResponseDto;
import couch.camping.controller.post.dto.response.PostRetrieveResponseDto;
import couch.camping.controller.post.dto.response.PostWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.repository.PostRepository;
import couch.camping.domain.postimage.entity.PostImage;
import couch.camping.domain.postimage.repository.PostImageRepository;
import couch.camping.domain.postlike.entity.PostLike;
import couch.camping.domain.postlike.repository.PostLikeRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostWriteResponseDto writePost(PostWriteRequestDto postWriteRequestDto, Member member) {
        Post post = Post.builder()
                .content(postWriteRequestDto.getContent())
                .postType(postWriteRequestDto.getPostType())
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
    public PostEditResponseDto editPost(Long postId, Member member, PostEditRequestDto postEditRequestDto) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "게시글 ID 에 맞는 게시글이 없습니다.");
                });

        if (findPost.getMember() != member) {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 리뷰가 아닙니다.");
        }

        findPost.editPost(postEditRequestDto.getContent(), postEditRequestDto.getPostType());
        
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
        }
    }

    /**
     * comment 필요
     */
    public PostRetrieveResponseDto retrievePost(Long postId, Member member) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_POST, "게시글 ID 에 맞는 게시글이 없습니다.");
                });

        List<PostImage> postImageList = postImageRepository.findByPostId(postId);

        return new PostRetrieveResponseDto(findPost, 0, postImageList);
    }

    public Page<PostRetrieveResponseDto> retrieveAllPost(String postType, Member member, Pageable pageable) {
        List<String> postTypeList = Arrays.asList("all", "free", "picture", "question");
        if (!postTypeList.contains(postType)) {
            throw new CustomException(ErrorCode.BAD_REQUEST_PARAM, "쿼리스트링 postType 의 값은 all, free, picture, question 만 가능합니다.");
        }

        return postRepository.findAllByIdWithPaging(postType, pageable)
                .map(post -> new PostRetrieveResponseDto(post, 0, post.getPostImageList()));
    }
}
