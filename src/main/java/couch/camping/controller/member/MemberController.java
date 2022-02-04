package couch.camping.controller.member;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.service.MemberService;
import couch.camping.message.request.RegisterInfo;
import couch.camping.message.response.MemberInfo;
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
    private final MemberService customUserDetailsService;

    @PostMapping("")
    public MemberInfo register(@RequestHeader("Authorization") String authorization,
                               @RequestBody RegisterInfo registerInfo) {
        log.info("##########register check log############");

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
        Member registeredUser = customUserDetailsService.register(
            decodedToken.getUid(), decodedToken.getEmail(), registerInfo.getNickname());
        return new MemberInfo(registeredUser);
    }

    /**
        질문, authentication 객체를 어떻게 받는지
     */
    @GetMapping("/me")
    public MemberInfo getUserMe(Authentication authentication) {
        log.info("@@@@@@@@login check log@@@@@@@@@@@@");
        Member member = ((Member) authentication.getPrincipal());
        return new MemberInfo(member);
    }
}
