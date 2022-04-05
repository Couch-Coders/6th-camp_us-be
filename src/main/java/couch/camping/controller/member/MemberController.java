package couch.camping.controller.member;

import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.member.dto.request.MemberRegisterRequestDto;
import couch.camping.controller.member.dto.request.MemberSaveRequestDto;
import couch.camping.controller.member.dto.response.MemberRegisterResponseDto;
import couch.camping.controller.member.dto.response.MemberRetrieveResponseDto;
import couch.camping.domain.camplike.repository.CampLikeRepository;
import couch.camping.domain.comment.service.CommentService;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.service.MemberRegister;
import couch.camping.domain.member.service.MemberService;
import couch.camping.domain.post.service.PostService;
import couch.camping.domain.review.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final ReviewService reviewService;
    private final PostService postService;
    private final CommentService commentService;
    private final CampLikeRepository campLikeCustomRepository;

    //로컬 회원 가입
    @ApiOperation(value = "로컬 회원 가입 API", notes = "로컬 개발 전용 회원 가입 API")
    @PostMapping("/local")
    public ResponseEntity<MemberRegisterResponseDto> registerLocalMember(@RequestBody @Valid MemberSaveRequestDto memberSaveRequestDto) {
        MemberRegister memberRegister = new MemberRegister(memberSaveRequestDto.getUid(), memberSaveRequestDto.getName(),
                memberSaveRequestDto.getEmail(), memberSaveRequestDto.getNickname(), memberSaveRequestDto.getImgUrl());

        MemberRegisterResponseDto responseDto = memberService.register(
                memberRegister);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }
    
    //회원 가입
    @ApiOperation(value = "회원 가입 API", notes = "파이어베이스 토큰을 Header 에 넣어 회원가입을 요청합니다.")
    @PostMapping("")
    public ResponseEntity<MemberRegisterResponseDto> registerMember(
            @ApiParam(value = "파이어베이스 인증 토큰", required = true) @RequestHeader("Authorization") String header,
            @ApiParam(value = "회원 닉네임 (요청 바디)", required = true) @RequestBody @Valid MemberRegisterRequestDto memberRegisterRequestDto) {
        // TOKEN을 가져온다.
        FirebaseToken decodedToken = memberService.decodeToken(header);
        // 사용자를 등록한다.
        MemberRegister memberRegister = new MemberRegister(decodedToken.getUid(), decodedToken.getName(),
                decodedToken.getEmail(), memberRegisterRequestDto.getNickname(), decodedToken.getPicture());

        MemberRegisterResponseDto responseDto = memberService.register(
                memberRegister);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }
    
    //로그인
    @ApiOperation(value = "로그인 API", notes = "파이어베이스 인증 토큰을 Header 에 넣어 로그인을 요청합니다.")
    @GetMapping("/me")
    public ResponseEntity<MemberRegisterResponseDto> login(Authentication authentication) {
        Member member = ((Member) authentication.getPrincipal());
        return ResponseEntity.ok(new MemberRegisterResponseDto(member));
    }
    
    //닉네임 수정
    @ApiOperation(value = "닉네임 수정 API", notes = "수정하고자 하는 회원의 토큰을 Header 에 넣고, 변경하고자 하는 nickname 을 요청 바디에 넣어주세요")
    @PatchMapping("/me")
    public ResponseEntity editMemberNickname(Authentication authentication, 
                                             @ApiParam(value = "수정하고자 하는 닉네임 (요청바디)", required = true)
                                             @RequestBody @Valid MemberRegisterRequestDto memberRegisterRequestDto) {
        memberService.editMemberNickName(((Member) authentication.getPrincipal()), memberRegisterRequestDto.getNickname());

        return ResponseEntity.noContent().build();
    }

    //단건 조회
    @ApiOperation(value = "마이 페이지 API", notes = "Header 의 토큰에 해당하는 회원의 정보를 조회합니다.")
    @GetMapping("/me/info")
    public ResponseEntity getMember(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        long reviewCount = reviewService.countMemberReviews(member.getId());
        long postCount = postService.countMemberPosts(member.getId());
        long commentCount = commentService.countMemberComments(member.getId());
        long campLikeCount = campLikeCustomRepository.countByMemberId(member.getId());

        return ResponseEntity.ok(new MemberRetrieveResponseDto(member, reviewCount, postCount, commentCount, campLikeCount));
    }
}
