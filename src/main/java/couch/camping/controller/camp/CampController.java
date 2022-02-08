package couch.camping.controller.camp;

import couch.camping.controller.camp.dto.request.CampSaveRequestDto;
import couch.camping.controller.camp.dto.request.CampListSaveRequestDto;
import couch.camping.controller.camp.dto.response.CampResponseDto;
import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.service.CampService;
import couch.camping.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public List<CampSearchResponseDto> getCamps(
            @RequestParam(defaultValue = "3") float rate,
            @RequestParam() String doNm,
            @RequestParam() String sigunguNm,
            @RequestParam() String name,
            @RequestParam(value = "sort1") String sortByDistance,
            @RequestParam(value = "sort2") String sortByLike,
            @RequestParam(required = false) String tag,
            @RequestParam() float x,
            @RequestParam() float y
            ) {


        List<CampSearchResponseDto> campList = campService.getCampList(rate, doNm, sigunguNm, sortByDistance, sortByLike, tag, x, y);
        return campList;

    }

    //camp 상세
    @GetMapping("/{campId}")
    public CampResponseDto campDetail(@PathVariable Long campId){
        return new CampResponseDto(campService.getCampDetail(campId));
    }
}
