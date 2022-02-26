package couch.camping.controller.member;

import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.controller.member.dto.request.MemberRegisterRequestDto;
import couch.camping.controller.member.dto.request.MemberSaveRequestDto;
import couch.camping.controller.member.dto.response.MemberRegisterResponseDto;
import couch.camping.controller.member.dto.response.MemberRetrieveResponseDto;
import couch.camping.controller.member.dto.response.MemberReviewsResponseDto;
import couch.camping.controller.member.dto.response.NotificationRetrieveResponseDto;
import couch.camping.domain.camp.service.CampService;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.service.MemberService;
import couch.camping.domain.notification.service.NotificationService;
import couch.camping.domain.review.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final ReviewService reviewService;
    private final CampService campService;
    private final NotificationService notificationService;

    //로컬 회원 가입
    @ApiOperation(value = "로컬 회원 가입 API", notes = "로컬 개발 전용 회원 가입 API")
    @PostMapping("/local")
    public ResponseEntity<MemberRegisterResponseDto> registerLocalMember(@RequestBody @Valid MemberSaveRequestDto memberSaveRequestDto) {
        MemberRegisterResponseDto responseDto = memberService.register(
                memberSaveRequestDto.getUid(), memberSaveRequestDto.getName()
                , memberSaveRequestDto.getEmail(), memberSaveRequestDto.getNickname(), memberSaveRequestDto.getImgUrl());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }
    
    //회원 가입
    @ApiOperation(value = "회원 가입 API", notes = "파이어베이스 토큰을 Header 에 넣어 회원가입을 요청합니다.")
    @PostMapping("")
    public ResponseEntity<MemberRegisterResponseDto> registerMember(
            @ApiParam(value = "파이어베이스 인증 토큰", required = true) @RequestHeader("Authorization") String header,
            @ApiParam(value = "회원 닉네임 (요청 바디)", required = true) @RequestBody @Valid MemberRegisterRequestDto memberRegisterRequestDto) {
        // TOKEN을 가져온다.
        FirebaseToken decodedToken = memberService.decodeToken(header);
        // 사용자를 등록한다.
        MemberRegisterResponseDto responseDto = memberService.register(
                decodedToken.getUid(), decodedToken.getName(), decodedToken.getEmail()
                , memberRegisterRequestDto.getNickname(), decodedToken.getPicture());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }
    
    //로그인
    @ApiOperation(value = "로그인 API", notes = "파이어베이스 인증 토큰을 Header 에 넣어 로그인을 요청합니다.")
    @GetMapping("/me")
    public ResponseEntity<MemberRegisterResponseDto> login(Authentication authentication) {
        Member member = ((Member) authentication.getPrincipal());
        return ResponseEntity.ok(new MemberRegisterResponseDto(member));
    }
    
    //닉네임 수정
    @ApiOperation(value = "닉네임 수정 API", notes = "수정하고자 하는 회원의 토큰을 Header 에 넣고, 변경하고자 하는 nickname 을 요청 바디에 넣어주세요")
    @PatchMapping("/me")
    public ResponseEntity editMemberNickname(Authentication authentication, 
                                             @ApiParam(value = "수정하고자 하는 닉네임 (요청바디)", required = true)
                                             @RequestBody @Valid MemberRegisterRequestDto memberRegisterRequestDto) {
        memberService.editMemberNickName(((Member) authentication.getPrincipal()), memberRegisterRequestDto.getNickname());

        return ResponseEntity.noContent().build();
    }

    //단건 조회
    @ApiOperation(value = "마이 페이지 API", notes = "Header 의 토큰에 해당하는 회원의 정보를 조회합니다.")
    @GetMapping("/me/info")
    public ResponseEntity getMember(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        long count = reviewService.countMemberReviews(member.getId());
        return ResponseEntity.ok(new MemberRetrieveResponseDto(member, count));
    }

    //회원이 작성한 리뷰 조회
    @ApiOperation(value = "회원이 작성한 리뷰 조회 API", notes = "Header 의 토큰에 해당하는 회원이 작성한 리뷰를 조회합니다.")
    @GetMapping("/me/reviews")
    public ResponseEntity<Page<MemberReviewsResponseDto>> getMemberReviews(Pageable pageable, Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();

        return ResponseEntity.ok(reviewService
                .retrieveMemberReviews(memberId, pageable).map(review-> new MemberReviewsResponseDto(review)));
    }
    
    //회원 알림 조회
    @ApiOperation(value = "회원 알림 조회 API", notes = "Header 의 토큰에 해당하는 회원의 전체 알림을 조회합니다.")
    @GetMapping("/me/notifications")
    public ResponseEntity<Page<NotificationRetrieveResponseDto>> getMemberNotifications(Pageable pageable,
                                                                                        Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(notificationService
                .retrieveNotifications(memberId, pageable));
    }

    //알림 단건 읽음
    @ApiOperation(value = "회원 알림 조회 API", notes = "Header 의 토큰에 해당하는 회원의 전체 알림을 조회합니다.")
    @PatchMapping("/me/notifications/{notificationId}")
    public ResponseEntity updateMemberNotification(@ApiParam(value = "알림 ID", required = true) @PathVariable Long notificationId,
                                                   Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        notificationService.updateNotification(member, notificationId);

        return ResponseEntity.noContent().build();
    }
    
    //회원의 안읽은 알림 개수
    @ApiOperation(value = "회원의 안읽은 알림 개수 API", notes = "Header 의 토큰에 해당하는 회원의 읽지 않은 알림을 조회합니다.")
    @GetMapping("/me/notifications/count")
    public ResponseEntity countMemberNotReadNotification(Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(notificationService.countMemberNotReadNotifications(memberId));


    }
    
    //알림 전체 읽음
    @ApiOperation(value = "회원 알림 전체 갱신 API", notes = "Header 의 토큰에 해당하는 회원의 전체 알림을 읽음으로 갱신합니다.")
    @PatchMapping("/me/notifications")
    public ResponseEntity updateMemberNotifications(Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();
        notificationService.updateNotifications(memberId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "회원의 모든 알림 삭제 API", notes = "회원의 모든 알림을 삭제합니다.")
    @DeleteMapping("/me/notifications")
    public ResponseEntity deleteAllNotification(Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();
        notificationService.deleteAllNotification(memberId);
        return ResponseEntity.noContent().build();
    }

    //회원이 좋아요한 캠핑장
    @ApiOperation(value = "회원이 좋아요한 캠핑장 조회 API",
            notes = "Header 의 토큰에 해당하는 회원이 좋아요한 캠핑장을 조회및 페이징. 쿼리스트링 예시(?page=0&size=10)")
    @GetMapping("/me/camps")
    public ResponseEntity<Page<CampSearchResponseDto>> MemberLikeCamps(Pageable pageable, Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();

        return ResponseEntity.ok(campService.getMemberLikeCamps(memberId, pageable));
    }
}
