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
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findById(username).get();
    }

    @Transactional
    public Member register(String uid, String name, String email, String nickname, String imgUrl) {
        Member member = Member.builder()
                .uid(uid)
                .name(name)
                .email(email)
                .nickname(nickname)
                .imgUrl(imgUrl)
                .build();
        memberRepository.save(member);
        return member;
    }
}
