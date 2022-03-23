package couch.camping.domain.member.service;


import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.repository.MemberRepository;
import couch.camping.exception.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MemberServiceTest {

    private static final String uid = "abcd";
    private static final String email = "abcd@daum.com";
    private static final String name = "성이름";
    private static final String nickname = "abcd";
    private static final String imgUrl = "https://www.balladang.com";

    @Mock
    MemberRepository memberRepository;
    
    @Spy
    @InjectMocks
    MemberService memberService;

    Member member;

    @BeforeEach
    void before() {
        member = Member.builder()
                .id(1L)
                .uid(uid)
                .email(email)
                .name(name)
                .nickname(nickname)
                .imgUrl(imgUrl)
                .build();
    }

    @Test
    @DisplayName("회원 조회 테스트")
    void loadByUserNameTest() {

        //when
        when(memberRepository.findByUid(any(String.class))).thenReturn(Optional.ofNullable(member));

        //then
        assertThat(((Member)memberService.loadUserByUsername(uid))).isEqualTo(member);
    }

    @Test
    @DisplayName("회원 조회 실패 테스트")
    void loadByUserNameNotFoundTest() {

        //when
        when(memberRepository.findByUid(any(String.class))).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(UsernameNotFoundException.class, () -> memberService.loadUserByUsername(uid));
    }
    
    @Test
    @DisplayName("회원 등록 테스트")
    void memberRegisterTest() {

        //when
        when(memberRepository.findByUid(any(String.class))).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        //then
        assertThat(memberService.register(uid, name, email, nickname, imgUrl).getUid()).isEqualTo(uid);
        assertThat(memberService.register(uid, name, email, nickname, imgUrl).getName()).isEqualTo(name);
        assertThat(memberService.register(uid, name, email, nickname, imgUrl).getEmail()).isEqualTo(email);
        assertThat(memberService.register(uid, name, email, nickname, imgUrl).getNickname()).isEqualTo(nickname);
        assertThat(memberService.register(uid, name, email, nickname, imgUrl).getImgUrl()).isEqualTo(imgUrl);
    }

    @Test
    @DisplayName("중복 회원 등록 테스트")
    void memberDuplicatedRegisterTest() {

        //when
        when(memberRepository.findByUid(any(String.class))).thenReturn(Optional.ofNullable(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        //then
        Assertions.assertThrows(CustomException.class,
                () -> memberService.register(uid, name, email, nickname, imgUrl));
    }
    
    @Test
    @DisplayName("회원 닉네임 수정 테스트")
    void memberEditNicknameTest() {

        //given
        String changingNickname = "changedName";

        //when
        //@InjectMock 객체를 스터빙 할 경우 @Spy, @Mock 을 사용
        doReturn(changingNickname).when(memberService).editMemberNickName(any(Member.class), any(String.class));

        //then
        assertThat(memberService.editMemberNickName(member, changingNickname)).isEqualTo(changingNickname);
    }
}