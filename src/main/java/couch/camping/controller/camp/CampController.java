package couch.camping.controller.camp;

import couch.camping.controller.camp.dto.request.CampSaveRequestDto;
import couch.camping.controller.camp.dto.request.CampListSaveRequestDto;
import couch.camping.controller.camp.dto.response.CampResponseDto;
import couch.camping.controller.camp.dto.response.CampSearchPagingResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.service.CampService;
import couch.camping.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@RestController
@RequestMapping("/camps")
@Slf4j
public class CampController {

    private final CampService campService;
    private final ModelMapper modelMapper;
    private final MemberService memberService;

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
    public CampSearchPagingResponseDto getCamps(
            Pageable pageable,
            @RequestParam(defaultValue = "3") float rate,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String tag
            ) {


        return campService.getCampList(pageable, rate, name, tag);
    }

    //camp 상세
    @GetMapping("/{campId}")
    public CampResponseDto campDetail(@PathVariable Long campId){
        return new CampResponseDto(campService.getCampDetail(campId));
    }
}
