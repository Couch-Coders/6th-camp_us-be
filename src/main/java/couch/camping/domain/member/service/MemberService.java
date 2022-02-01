package couch.camping.domain.member.service;

import couch.camping.domain.member.repository.MemberRepository;
import couch.camping.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findById(username).get();
    }

    @Transactional
    public Member register(String uid, String email, String nickname) {
        Member member = Member.builder()
                .username(uid)
                .email(email)
                .nickname(nickname)
                .build();
        memberRepository.save(member);
        return member;
    }
}
