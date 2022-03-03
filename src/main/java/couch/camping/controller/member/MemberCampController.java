package couch.camping.controller.member;

import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.service.CampService;
import couch.camping.domain.member.entity.Member;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberCampController {

    private final CampService campService;

    //회원이 좋아요한 캠핑장
    @ApiOperation(value = "회원이 좋아요한 캠핑장 조회 API",
            notes = "Header 의 토큰에 해당하는 회원이 좋아요한 캠핑장을 조회및 페이징. 쿼리스트링 예시(?page=0&size=10)")
    @GetMapping("/me/camps")
    public ResponseEntity<Page<CampSearchResponseDto>> MemberLikeCamps(Pageable pageable, Authentication authentication) {
        Long memberId = ((Member) authentication.getPrincipal()).getId();

        return ResponseEntity.ok(campService.getMemberLikeCamps(memberId, pageable));
    }
}
