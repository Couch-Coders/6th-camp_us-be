package couch.camping.controller.member;

import couch.camping.controller.member.dto.response.MemberPostResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.post.service.post.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberPostController {

    private final PostService postService;

    //회원이 작성한 게시글 조회
    @ApiOperation(value = "회원이 작성한 게시글 조회 API", notes = "Header 의 토큰에 해당하는 회원이 작성한 게시글을 조회합니다.")
    @GetMapping("/me/posts")
    public ResponseEntity<Page<MemberPostResponseDto>> getMemberPost(Pageable pageable, Authentication authentication){
        Member member = (Member) authentication.getPrincipal();

        return ResponseEntity.ok(postService.retrieveMemberComment(member, pageable));
    }

}
