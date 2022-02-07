package couch.camping.controller.member;

import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.member.dto.request.MemberSaveRequestDto;
import couch.camping.controller.member.dto.request.MemberRegisterRequestDto;
import couch.camping.controller.member.dto.response.MemberRetrieveResponseDto;
import couch.camping.controller.member.dto.response.MemberRegisterResponseDto;
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
    public ResponseEntity<MemberRegisterResponseDto> registerLocalMember(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {
        Member registeredMember = memberService.register(
                memberSaveRequestDto.getUid(), memberSaveRequestDto.getName()
                , memberSaveRequestDto.getEmail(), memberSaveRequestDto.getNickname(), memberSaveRequestDto.getImgUrl());

        return new ResponseEntity<MemberRegisterResponseDto>(new MemberRegisterResponseDto(registeredMember), HttpStatus.CREATED);
    }
    
    //회원 가입
    @PostMapping("")
    public ResponseEntity<MemberRegisterResponseDto> registerMember(@RequestHeader("Authorization") String header,
                                                                    @RequestBody MemberRegisterRequestDto memberRegisterRequestDto) {
        // TOKEN을 가져온다.
        FirebaseToken decodedToken = memberService.decodeToken(header);
        // 사용자를 등록한다.
        Member registeredMember = memberService.register(
            decodedToken.getUid(), decodedToken.getName(), decodedToken.getEmail()
                , memberRegisterRequestDto.getNickname(), decodedToken.getPicture());

        return new ResponseEntity<MemberRegisterResponseDto>(new MemberRegisterResponseDto(registeredMember), HttpStatus.CREATED);
    }
    
    //로그인
    @GetMapping("/me")
    public ResponseEntity<MemberRegisterResponseDto> login(Authentication authentication) {
        Member member = ((Member) authentication.getPrincipal());
        return ResponseEntity.ok(new MemberRegisterResponseDto(member));
    }
    
    //닉네임 수정
    @PatchMapping("/me")
    public ResponseEntity editMemberNickname(Authentication authentication,
                                       @RequestBody MemberRegisterRequestDto memberRegisterRequestDto) {
        Member member = ((Member) authentication.getPrincipal());
        memberService.editMemberNickName(member.getId(), memberRegisterRequestDto.getNickname());

        return ResponseEntity.noContent().build();
    }

    //단건 조회
    @GetMapping("/me/info")
    public ResponseEntity getMember(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        return ResponseEntity.ok(new MemberRetrieveResponseDto(member));
    }

}
