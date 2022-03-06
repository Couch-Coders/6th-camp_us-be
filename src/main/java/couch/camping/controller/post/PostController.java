package couch.camping.controller.post;

import couch.camping.controller.post.dto.request.PostWriteRequestDto;
import couch.camping.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity writePost(@RequestBody PostWriteRequestDto postWriteRequestDto) {
        return ResponseEntity.ok(postService.writePost(postWriteRequestDto));
    }

}
