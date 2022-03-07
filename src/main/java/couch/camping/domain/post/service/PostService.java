package couch.camping.domain.post.service;

import couch.camping.controller.post.dto.request.PostEditRequestDto;
import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.controller.post.dto.response.PostEditResponseDto;
import couch.camping.controller.post.dto.response.PostWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.repository.PostRepository;
import couch.camping.domain.postimage.entity.PostImage;
import couch.camping.domain.postimage.repository.PostImageRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    @Transactional
    public PostWriteResponseDto writePost(PostWriteRequestDto postWriteRequestDto, Member member) {
        Post post = Post.builder()
                .content(postWriteRequestDto.getContent())
                .hashTag(postWriteRequestDto.getHashTag())
                .member(member)
                .build();

        Post savePost = postRepository.save(post);
        
        List<PostImage> postImageList = new ArrayList<>();
        for (String imgUrl : postWriteRequestDto.getImgUrlList()) {
            PostImage postImage = PostImage.builder()
                    .post(savePost)
                    .member(member)
                    .imgUrl(imgUrl)
                    .build();
            postImageList.add(postImage);
            postImageRepository.save(postImage);
        }

        return new PostWriteResponseDto(savePost, postImageList);
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

        findPost.editPost(postEditRequestDto.getContent(), postEditRequestDto.getHashTag());
        
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
            postImageRepository.save(postImage);
        }

        return new PostEditResponseDto(findPost, postImageList);

    }
}
