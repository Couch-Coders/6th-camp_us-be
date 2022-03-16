package couch.camping.controller.comment;

import couch.camping.controller.comment.dto.request.CommentEditRequestDto;
import couch.camping.controller.comment.dto.response.CommentEditResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveResponseDto;
import couch.camping.domain.comment.service.CommentService;
import couch.camping.domain.member.entity.Member;
import couch.camping.util.RequestUtil;
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
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentRetrieveResponseDto> retrieveComment(@PathVariable Long commentId,
                                                                      HttpServletRequest request){
        String header = RequestUtil.getAuthorizationToken(request);
        return ResponseEntity.ok(commentService.retrieveComment(commentId, header));
    }

    //댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentEditResponseDto> editComment(@RequestBody CommentEditRequestDto commentEditRequestDto,
                                                              @PathVariable Long commentId,
                                                              Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        return new ResponseEntity(commentService.editComment(commentEditRequestDto, member, commentId), HttpStatus.OK);
    }

    //댓글 좋아요
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity likeComment(@PathVariable Long commentId,
                                      Authentication authentication){

        Member member = (Member) authentication.getPrincipal();
        commentService.likeComment(commentId, member);
        return ResponseEntity.noContent().build();
    }
    
    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId,
                                        Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        commentService.deleteComment(commentId, member);
        return ResponseEntity.noContent().build();
    }

}
