package couch.camping.controller.member;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.member.dto.request.MemberSaveRequestDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.service.MemberService;
import couch.camping.controller.member.dto.request.RegisterRequestDto;
import couch.camping.controller.member.dto.response.RegisterResponseDto;
import couch.camping.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final FirebaseAuth firebaseAuth;
    private final MemberService memberService;

    //로컬 회원 가입 api
    @PostMapping("/local/save")
    public MemberSaveRequestDto save(@RequestBody MemberSaveRequestDto memberSaveRequestDto) {
        memberService.register(
                memberSaveRequestDto.getUid(), memberSaveRequestDto.getName()
                , memberSaveRequestDto.getEmail(), memberSaveRequestDto.getNickname(), memberSaveRequestDto.getImgUrl());
        return memberSaveRequestDto;
    }

    @PostMapping("")
    public RegisterResponseDto register(@RequestHeader("Authorization") String authorization,
                                        @RequestBody RegisterRequestDto registerRequestDto) {
        // TOKEN을 가져온다.
        FirebaseToken decodedToken;
        try {
            String token = RequestUtil.getAuthorizationToken(authorization);
            decodedToken = firebaseAuth.verifyIdToken(token);
        } catch (IllegalArgumentException | FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
                "{\"code\":\"INVALID_TOKEN\", \"message\":\"" + e.getMessage() + "\"}");
        }
        // 사용자를 등록한다.
        Member registeredMember = memberService.register(
            decodedToken.getUid(), decodedToken.getName(), decodedToken.getEmail(), registerRequestDto.getNickname(), decodedToken.getPicture());
        return new RegisterResponseDto(registeredMember);
    }

    @GetMapping("/me")
    public RegisterResponseDto getUserMe(Authentication authentication) {
        Member member = ((Member) authentication.getPrincipal());
        return new RegisterResponseDto(member);
    }
}
