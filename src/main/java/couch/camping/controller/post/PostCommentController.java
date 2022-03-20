package couch.camping.controller.post;

import couch.camping.controller.comment.dto.request.CommentWriteRequestDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveResponseDto;
import couch.camping.controller.comment.dto.response.CommentWriteResponseDto;
import couch.camping.domain.comment.service.CommentService;
import couch.camping.domain.member.entity.Member;
import couch.camping.util.RequestUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class PostCommentController {

    private final CommentService commentService;

    //게시글의 댓글 전체 조회
    @ApiOperation(value = "커뮤니티 댓글 전체 조회 API", notes = "게시글 ID를 통해 댓글 전체를 조회합니다. 쿼리스트링 예시(?page=0&size=10)")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<CommentRetrieveResponseDto>> retrieveAllComment(@ApiParam(value = "게시글 ID", required = true) @PathVariable Long postId,
                                                                               HttpServletRequest request,
                                                                               Pageable pageable){
        String header = RequestUtil.getAuthorizationToken(request);
        return ResponseEntity.ok(commentService.retrieveAllComment(postId, header, pageable));
    }

    //댓글 작성
    @ApiOperation(value = "커뮤니티 댓글 작성 API", notes = "게시글 ID를 통해 해당 게시글에 댓글을 작성합니다.")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentWriteResponseDto> writeComment(@RequestBody CommentWriteRequestDto commentWriteRequestDto,
                                                                @ApiParam(value = "게시글 ID", required = true) @PathVariable Long postId,
                                                                Authentication authentication){
        Member member = (Member) authentication.getPrincipal();
        return new ResponseEntity(commentService.writeComment(commentWriteRequestDto, member, postId), HttpStatus.CREATED);
    }

}
