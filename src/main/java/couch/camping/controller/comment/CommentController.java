package couch.camping.controller.comment;

import couch.camping.controller.comment.dto.request.CommentEditRequestDto;
import couch.camping.controller.comment.dto.response.CommentEditResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveResponseDto;
import couch.camping.domain.comment.service.CommentService;
import couch.camping.domain.member.entity.Member;
import couch.camping.util.RequestUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 단건 조회
    @ApiOperation(value = "커뮤니티 댓글 조회 API", notes = "댓글 ID를 통해 댓글을 조회합니다.")
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentRetrieveResponseDto> retrieveComment(@ApiParam(value = "댓글 ID", required = true) @PathVariable Long commentId,
                                                                      HttpServletRequest request){
        String header = RequestUtil.getAuthorizationToken(request);
        return ResponseEntity.ok(commentService.retrieveComment(commentId, header));
    }

    //댓글 수정
    @PutMapping("/comments/{commentId}")
    @ApiOperation(value = "커뮤니티 댓글 수정 API", notes = "댯글 ID를 통해 작성된 댓글을 수정합니다.")
    public ResponseEntity<CommentEditResponseDto> editComment(@RequestBody CommentEditRequestDto commentEditRequestDto,
                                                              @ApiParam(value = "댓글 ID", required = true) @PathVariable Long commentId,
                                                              Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        return new ResponseEntity(commentService.editComment(commentEditRequestDto, member, commentId), HttpStatus.OK);
    }

    //댓글 좋아요
    @PatchMapping("/comments/{commentId}")
    @ApiOperation(value = "커뮤니티 댓글 좋아요 API", notes = "댓글 ID를 통해 해당 댓글에 좋아요를 합니다.")
    public ResponseEntity likeComment(@ApiParam(value = "댓글 ID", required = true) @PathVariable Long commentId,
                                      Authentication authentication){

        Member member = (Member) authentication.getPrincipal();
        commentService.likeComment(commentId, member);
        return ResponseEntity.noContent().build();
    }
    
    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    @ApiOperation(value = "커뮤니티 댓글 삭제 API", notes = "댓글 ID를 통해 해당 댓글을 삭제합니다.")
    public ResponseEntity deleteComment(@ApiParam(value = "댓글 ID", required = true) @PathVariable Long commentId,
                                        Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        commentService.deleteComment(commentId, member);
        return ResponseEntity.noContent().build();
    }

}
