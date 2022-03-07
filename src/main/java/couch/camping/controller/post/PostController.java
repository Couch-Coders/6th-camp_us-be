package couch.camping.controller.post;

import couch.camping.controller.post.dto.request.PostEditRequestDto;
import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity writePost(@RequestBody PostWriteRequestDto postWriteRequestDto, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        return ResponseEntity.ok(postService.writePost(postWriteRequestDto, member));
    }

    @PutMapping("/{postId}")
    public ResponseEntity editPost(@PathVariable Long postId, @RequestBody PostEditRequestDto postEditRequestDto,
                                   Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        return ResponseEntity.ok(postService.editPost(postId, member, postEditRequestDto));
    }
}
