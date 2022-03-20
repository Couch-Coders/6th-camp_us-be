package couch.camping.controller.post;

import couch.camping.controller.post.dto.request.PostEditRequestDto;
import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.controller.post.dto.response.PostEditResponseDto;
import couch.camping.controller.post.dto.response.PostWriteResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.post.service.PostService;
import couch.camping.util.RequestUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    
    private final PostService postService;
    
    //게시글 작성
    @ApiOperation(value = "커뮤니티 게시글 작성 API", notes = "커뮤니티에 게시글을 작성합니다.")
    @PostMapping("")
    public ResponseEntity<PostWriteResponseDto> writePost(@RequestBody PostWriteRequestDto postWriteRequestDto, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        return new ResponseEntity(postService.writePost(postWriteRequestDto, member), HttpStatus.CREATED);
    }

    //게시글 단건 조회
    @ApiOperation(value = "커뮤니티 게시글 단건 조회 API", notes = "게시글 ID를 통해 특정 게시글을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity retrievePost(@ApiParam(value = "게시글 ID", required = true) @PathVariable Long postId, HttpServletRequest request) {
        String header = RequestUtil.getAuthorizationToken(request);
        return ResponseEntity.ok(postService.retrievePost(postId, header));
    }

    //게시글 전체 조회
    @GetMapping("")
    @ApiOperation(value = "커뮤니티 게시글 전체 조회 API", notes = "커뮤니티에서 모든 게시글을 조회합니다. 쿼리스트링 예시(?page=0&size=10)")
    public ResponseEntity retrieveAllPost(Pageable pageable, @ApiParam(value = "게시글 분류", required = false) @RequestParam(defaultValue = "all") String postType,
                                          HttpServletRequest request) {
        String header = RequestUtil.getAuthorizationToken(request);
        return ResponseEntity.ok(postService.retrieveAllPost(postType, pageable, header));
    }
    
    //게시글 수정
    @PutMapping("/{postId}")
    @ApiOperation(value = "커뮤니티 게시글 수정 API", notes = "게시글 ID를 통해 작성된 게시글을 수정합니다.")
    public ResponseEntity<PostEditResponseDto> editPost(@ApiParam(value = "게시글 ID", required = true) @PathVariable Long postId, @RequestBody PostEditRequestDto postEditRequestDto,
                                                        Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        return new ResponseEntity(postService.editPost(postId, member, postEditRequestDto), HttpStatus.OK);
    }
    
    //게시글 좋아요
    @PatchMapping("/{postId}")
    @ApiOperation(value = "커뮤니티 게시글 좋아요 API", notes = "게시글 ID를 통해 작성된 게시글에 좋아요를 합니다.")
    public ResponseEntity likePost(@ApiParam(value = "게시글 ID", required = true) @PathVariable Long postId, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        postService.likePost(postId, member);
        return ResponseEntity.noContent().build();
    }
    
    //베스트 게시글 조회
    @GetMapping("/best")
    @ApiOperation(value = "커뮤니티 베스트 게시글 조회 API", notes = "게시글 중 좋아요가 가장 많은 게시글을 불러옵니다. 쿼리스트링 예시(?page=0&size=10)")
    public ResponseEntity retrieveAllBestPost(Pageable pageable) {
        return ResponseEntity.ok(postService.retrieveAllBestPost(pageable));
    }
    
    //게시글 삭제
    @DeleteMapping("/{postId}")
    @ApiOperation(value = "커뮤니티 게시글 삭제 API", notes = "게시글 ID를 통해 작성된 게시글을 삭제합니다.")
    public ResponseEntity deletePost(@ApiParam(value = "게시글 ID", required = true) @PathVariable Long postId, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        postService.deletePost(postId, member);
        return ResponseEntity.noContent().build();
    }
}

