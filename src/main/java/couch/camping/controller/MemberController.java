package couch.camping.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.service.MemberService;
import couch.camping.message.request.RegisterInfo;
import couch.camping.message.response.MemberInfo;
import couch.camping.util.RequestUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberController {
    private final FirebaseAuth firebaseAuth;
    private final MemberService customUserDetailsService;

    @PostMapping("")
    public MemberInfo register(@RequestHeader("Authorization") String authorization,
                               @RequestBody RegisterInfo registerInfo) {
        // TOKEN을 가져온다.
        FirebaseToken decodedToken;
        try {
            String token = RequestUtility.getAuthorizationToken(authorization);
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

    @GetMapping("/me")
    public MemberInfo getUserMe(Authentication authentication) {
        Member member = ((Member) authentication.getPrincipal());
        return new MemberInfo(member);
    }
}
