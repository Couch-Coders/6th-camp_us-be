package couch.camping.controller.post;

import couch.camping.controller.post.dto.request.PostEditRequestDto;
import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.controller.post.dto.response.PostEditResponseDto;
import couch.camping.controller.post.dto.response.PostWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    
    private final PostService postService;
    
    //게시글 작성
    @PostMapping("")
    public ResponseEntity<PostWriteResponseDto> writePost(@RequestBody PostWriteRequestDto postWriteRequestDto, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        return new ResponseEntity(postService.writePost(postWriteRequestDto, member), HttpStatus.CREATED);
    }

    //게시글 단건 조회
    @GetMapping("/{postId}")
    public ResponseEntity retrievePost(@PathVariable Long postId, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        return ResponseEntity.ok(postService.retrievePost(postId, member));
    }

    //게시글 전체 조회
    @GetMapping("")
    public ResponseEntity retrieveAllPost(@RequestParam String postType, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        return ResponseEntity.ok(postService.retrieveAllPost(postType, member));
    }
    
    //게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostEditResponseDto> editPost(@PathVariable Long postId, @RequestBody PostEditRequestDto postEditRequestDto,
                                                        Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        return new ResponseEntity(postService.editPost(postId, member, postEditRequestDto), HttpStatus.OK);
    }
    
    //게시글 좋아요
    @PatchMapping("/{postId}")
    public ResponseEntity likePost(@PathVariable Long postId, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        postService.likePost(postId, member);
        return ResponseEntity.noContent().build();
    }
}
