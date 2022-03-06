package couch.camping.domain.post.service;

import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.controller.post.dto.response.PostWriteResponseDto;
import couch.camping.domain.post.entity.Post;
import couch.camping.domain.post.repository.PostRepository;
import couch.camping.domain.postimage.entity.PostImage;
import couch.camping.domain.postimage.repository.PostImageRepository;
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
    public PostWriteResponseDto writePost(PostWriteRequestDto postWriteRequestDto) {
        Post post = new Post(postWriteRequestDto);
        Post savePost = postRepository.save(post);
        
        List<PostImage> postImageList = new ArrayList<>();
        for (String imgUrl : postWriteRequestDto.getImgUrlList()) {
            PostImage postImage = new PostImage(savePost, imgUrl);
            postImageList.add(postImage);
            postImageRepository.save(postImage);
        }

        return new PostWriteResponseDto(savePost, postImageList);
    }

}
