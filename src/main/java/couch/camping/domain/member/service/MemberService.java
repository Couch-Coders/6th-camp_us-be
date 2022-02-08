package couch.camping.domain.member.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.member.dto.request.MemberReviewRequestDto;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.repository.MemberRepository;
import couch.camping.domain.review.entity.Review;
import couch.camping.domain.review.repository.ReviewRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import couch.camping.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
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

    @Transactional
    public void editMemberNickName(Long id, String nickname) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_USER, "회원 ID 에 해당하는 회원이 없습니다.");
                });
        member.changeNickname(nickname);
    }

    //헤더에서
    public FirebaseToken decodeToken(String header) {
        FirebaseToken decodedToken = null;

        try {
            String token = RequestUtil.getAuthorizationToken(header);
            decodedToken = firebaseAuth.verifyIdToken(token);
        } catch (IllegalArgumentException | FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "{\"code\":\"INVALID_TOKEN\", \"message\":\"" + e.getMessage() + "\"}");
        }
        return decodedToken;
    }

    public Page<Review> retrieveMemberReviews(Long memberId, MemberReviewRequestDto memberReviewRequestDto) {
        int page = memberReviewRequestDto.getPage();
        int size = memberReviewRequestDto.getSize();
        int sort = memberReviewRequestDto.getSort();

        Sort.Direction sortType;
        if (sort == 0) sortType = Sort.Direction.DESC;
        else sortType = Sort.Direction.ASC;

        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by(sortType, "createdDate"));
        Page<Review> reviewPage = reviewRepository.findByMemberId(pageRequest, memberId);

        return reviewPage;
    }
}
