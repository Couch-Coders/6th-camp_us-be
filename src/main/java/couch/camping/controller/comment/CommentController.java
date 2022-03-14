package couch.camping.controller.comment;

import couch.camping.controller.comment.dto.request.CommentEditRequestDto;
import couch.camping.controller.comment.dto.request.CommentWriteRequestDto;
import couch.camping.controller.comment.dto.response.CommentEditResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveAllResponseDto;
import couch.camping.controller.comment.dto.response.CommentRetrieveResponseDto;
import couch.camping.controller.comment.dto.response.CommentWriteResponseDto;
import couch.camping.domain.comment.service.CommentService;
import couch.camping.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 단건 조회
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommentRetrieveResponseDto> retrieveComment(@PathVariable Long commentId,
                                                                      Authentication authentication){
        Member member = (Member) authentication.getPrincipal();
        return ResponseEntity.ok(commentService.retrieveComment(commentId, member));
    }

    //게시글의 댓글 전체 조회
    @GetMapping("posts/{postId}/comment")
    public ResponseEntity<Page<CommentRetrieveAllResponseDto>> retrieveAllComment(@PathVariable Long postId,
                                                                                 Pageable pageable){
        return ResponseEntity.ok(commentService.retrieveAllComment(postId, pageable));
    }


    //댓글 작성
    @PostMapping("/posts/{postId}/comment") //이게 코멘트 컨트롤러에 있는 게 맞나?
    public ResponseEntity<CommentWriteResponseDto> writeComment(@RequestBody CommentWriteRequestDto commentWriteRequestDto,
                                                                @PathVariable Long postId,
                                                                Authentication authentication){
        Member member = (Member) authentication.getPrincipal();
        return new ResponseEntity(commentService.writeComment(commentWriteRequestDto, member, postId), HttpStatus.CREATED);
    }

    //댓글 수정
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<CommentEditResponseDto> editComment(@RequestBody CommentEditRequestDto commentEditRequestDto,
                                                              @PathVariable Long commentId,
                                                              Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        return new ResponseEntity(commentService.editComment(commentEditRequestDto, member, commentId), HttpStatus.OK);
    }

    //댓글 좋아요
    @PatchMapping("/comment/{commentId}")
    public ResponseEntity likeComment(@PathVariable Long commentId,
                                      Authentication authentication){

        Member member = (Member) authentication.getPrincipal();
        commentService.likeComment(commentId, member);
        return ResponseEntity.noContent().build();
    }
}
