package couch.camping.controller.member;

import couch.camping.controller.member.dto.response.MemberCommentsResponseDto;
import couch.camping.domain.comment.service.CommentService;
import couch.camping.domain.member.entity.Member;
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
public class MemberCommentController {

    private final CommentService commentService;

    //회원이 작성한 댓글 조회
    @ApiOperation(value = "회원이 작성한 댓글 조회 API", notes = "Header 의 토큰에 해당하는 회원이 작성한 댓글을 조회합니다.")
    @GetMapping("/me/comment")
    public ResponseEntity<Page<MemberCommentsResponseDto>> getMemberComments(Pageable pageable, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        return ResponseEntity.ok(commentService.retrieveMemberComment(member, pageable));
    }

}
