package couch.camping.controller.camp;

import couch.camping.controller.camp.dto.request.CampListSaveRequestDto;
import couch.camping.controller.camp.dto.request.CampSaveRequestDto;
import couch.camping.controller.camp.dto.response.CampResponseDto;
import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.service.CampService;
import couch.camping.domain.member.entity.Member;
import couch.camping.util.RequestUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
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

    @Profile("local")
    @ApiOperation(value = "캠핑장 데이터 저장 API", notes = "배포용으로 쓰이지 않습니다. 로컬에서만 데이터를 집어넣을 수 있습니다.")
    @PostMapping("")
    public String save(@RequestBody CampListSaveRequestDto campListSaveRequestDto) {

        for (CampSaveRequestDto campSaveRequestDto : campListSaveRequestDto.getItem()) {
            Camp map = modelMapper.map(campSaveRequestDto, Camp.class);
            campService.save(map);
        }
        return "ok";
    }

    //camp 검색
    @ApiOperation(value = "캠핑장 검색 API", notes = "이름, 시.군.구, 검색 태그, 현재 위치 등 캠핑장 검색")
    @GetMapping("")
    public ResponseEntity<Page<CampSearchResponseDto>> getCamps(
            @ApiParam(value = "페이징 모델", required = true) Pageable pageable,
            @ApiParam(value = "캠핑장 이름") @RequestParam(required = false) String name,
            @ApiParam(value = "캠핑장 시.군.구") @RequestParam(required = false) String sigunguNm,
            @ApiParam(value = "시.도") @RequestParam(required = false) String doNm,
            @ApiParam(value = "캠핑장 검색 태그") @RequestParam(required = false) String tag,
            @ApiParam(value = "현재 X 좌표") @RequestParam(defaultValue = "127.0016985") Float mapX,
            @ApiParam(value = "현재 Y 좌표") @RequestParam(defaultValue = "37.5642135") Float mapY,
            @ApiParam(value = "정렬 방식 (rate, distance) 기본 값 distance") @RequestParam(defaultValue = "rate") String sort,

            HttpServletRequest request
            ) {
        String header = RequestUtil.getAuthorizationToken(request);

        return ResponseEntity.ok(campService.getCampList(pageable, name, doNm, sigunguNm, tag, header, sort, mapX, mapY));
    }

    //camp 상세
    @ApiOperation(value = "캠핑장 상세 검색 API", notes = "경로 변수에 캠핑장의 ID 를 넣어 조회합니다.")
    @GetMapping("/{campId}")
    public CampResponseDto campDetail(
            @ApiParam(value = "캠핑장 ID", required = true) @PathVariable Long campId){
        return new CampResponseDto(campService.getCampDetail(campId));
    }

    //camp 좋아요
    @ApiOperation(value = "캠핑장 좋아요 API", notes = "경로 변수에 캠핑장의 ID 를 넣어 조회합니다.(로그인 후 이용 가능)")
    @PatchMapping("/{campId}/like")
    public ResponseEntity likeCamp(
            @ApiParam(value = "캠핑장 ID", required = true) @PathVariable Long campId,
            Authentication authentication) {

        campService.likeCamp(campId, (Member)authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }
}
