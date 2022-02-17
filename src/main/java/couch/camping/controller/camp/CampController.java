package couch.camping.controller.camp;

import couch.camping.controller.camp.dto.request.CampListSaveRequestDto;
import couch.camping.controller.camp.dto.request.CampSaveRequestDto;
import couch.camping.controller.camp.dto.response.CampResponseDto;
import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.service.CampService;
import couch.camping.domain.member.entity.Member;
import couch.camping.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/camps")
@Slf4j
public class CampController {

    private final CampService campService;
    private final ModelMapper modelMapper;

    @PostMapping("")
    public String save(@RequestBody CampListSaveRequestDto campListSaveRequestDto) {

        for (CampSaveRequestDto campSaveRequestDto : campListSaveRequestDto.getItem()) {
            Camp map = modelMapper.map(campSaveRequestDto, Camp.class);
            campService.save(map);
        }
        return "ok";
    }

    //camp 검색
    //?rate=4.5&doNm=경상도&sigunguNm=창원군&name=달빛캠핑장&sort1=distance&sort2=like&tag=와이파이_전기_하수도_수영장&x=3.1423&y=3.141592
    @GetMapping("")
    public Page<CampSearchResponseDto> getCamps(
            Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sigunguNm,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "rate") String sort,
            HttpServletRequest request
            ) {
        String header = RequestUtil.getAuthorizationToken(request);

        return campService.getCampList(pageable, name, sigunguNm, tag, header, sort);
    }

    //camp 상세
    @GetMapping("/{campId}")
    public CampResponseDto campDetail(@PathVariable Long campId){
        return new CampResponseDto(campService.getCampDetail(campId));
    }

    //camp 좋아요
    @PatchMapping("/{campId}/like")
    public ResponseEntity likeCamp(@PathVariable Long campId, Authentication authentication) {

        campService.likeCamp(campId, (Member)authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }
}
