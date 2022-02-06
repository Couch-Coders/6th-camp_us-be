package couch.camping.controller.member;

import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.member.dto.request.MemberSaveRequestDto;
import couch.camping.controller.member.dto.request.RegisterRequestDto;
import couch.camping.controller.member.dto.response.RegisterResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    //로컬 회원 가입
    @PostMapping("/local")
    public ResponseEntity<RegisterResponseDto> registerLocal(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {
        Member registeredMember = memberService.register(
                memberSaveRequestDto.getUid(), memberSaveRequestDto.getName()
                , memberSaveRequestDto.getEmail(), memberSaveRequestDto.getNickname(), memberSaveRequestDto.getImgUrl());

        return new ResponseEntity<RegisterResponseDto>(new RegisterResponseDto(registeredMember), HttpStatus.CREATED);
    }
    
    //회원 가입
    @PostMapping("")
    public ResponseEntity<RegisterResponseDto> register(@RequestHeader("Authorization") String header,
                                        @RequestBody RegisterRequestDto registerRequestDto) {
        // TOKEN을 가져온다.
        FirebaseToken decodedToken = memberService.decodeToken(header);
        // 사용자를 등록한다.
        Member registeredMember = memberService.register(
            decodedToken.getUid(), decodedToken.getName(), decodedToken.getEmail()
                , registerRequestDto.getNickname(), decodedToken.getPicture());

        return new ResponseEntity<RegisterResponseDto>(new RegisterResponseDto(registeredMember), HttpStatus.CREATED);
    }
    
    //로그인
    @GetMapping("/me")
    public ResponseEntity<RegisterResponseDto> login(Authentication authentication) {
        Member member = ((Member) authentication.getPrincipal());
        return ResponseEntity.ok(new RegisterResponseDto(member));
    }
    
    //닉네임 수정
    @PatchMapping("/me")
    public ResponseEntity editNickname(Authentication authentication,
                                       @RequestBody RegisterRequestDto registerRequestDto) {
        Member member = ((Member) authentication.getPrincipal());
        memberService.editMemberNickName(member.getUid(), registerRequestDto.getNickname());

        return ResponseEntity.noContent().build();
    }
}
