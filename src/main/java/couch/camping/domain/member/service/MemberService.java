package couch.camping.domain.member.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.member.dto.response.MemberRegisterResponseDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.repository.MemberRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import couch.camping.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final FirebaseAuth firebaseAuth;
    
    //스프링 시큐리티에서 DB 에서 uid 를 조회
    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        return memberRepository.findByUid(uid)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("해당 회원이 존재하지 않습니다.");
                });
    }
    
    //회원 등록
    @Transactional
    public MemberRegisterResponseDto register(MemberRegister memberRegister) {
        validateAlreadyRegistered(memberRegister);
        return new MemberRegisterResponseDto(memberRepository.save(createMember(memberRegister)));
    }

    private Member createMember(MemberRegister memberRegister) {
        Member member = Member.builder()
                .uid(memberRegister.getUid())
                .name(memberRegister.getName())
                .email(memberRegister.getEmail())
                .nickname(memberRegister.getNickname())
                .imgUrl(memberRegister.getImgUrl())
                .build();
        return member;
    }

    private void validateAlreadyRegistered(MemberRegister memberRegister) {
        Optional<Member> optionalMember = memberRepository.findByUid(memberRegister.getUid());
        if (optionalMember.isPresent()) {
            throw new CustomException(ErrorCode.EXIST_MEMBER, "해당 계정으로 이미 회원가입을 했습니다.");
        }
    }

    @Transactional
    public String editMemberNickName(Member member, String nickname) {
        member.changeNickname(nickname);
        return member.getNickname();
    }

    //헤더에서
    public FirebaseToken decodeToken(String header) {
        try {
            String token = RequestUtil.getAuthorizationToken(header);
            return firebaseAuth.verifyIdToken(token);
        } catch (IllegalArgumentException | FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "{\"code\":\"INVALID_TOKEN\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }

}
