package couch.camping.domain.camp.service;

import couch.camping.controller.camp.dto.response.CampResponseDto;
import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.CampRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CampService {

    private final CampRepository campRepository;

    @Transactional
    public void save(Camp camp) {
        campRepository.save(camp);
    }

    //캠핑장 단건 조회
    public Camp getCampDetail(Long campId) {
        Camp findCamp = campRepository.findById(campId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_CAMP, "campId 에 맞는 캠핑장이 없습니다.");
                });
        return findCamp;
    }

//    "sbrsCl": "전기,무선인터넷,장작판매,온수,물놀이장,산책로,운동장,운동시설"

    //캠핑장 조건 다중 조회
    public List<CampSearchResponseDto> getCampList(
            float rate, String doNm, String sigunguNm, String sortByDistance, String sortByLike, String tag, float x, float y) {
        List<Camp> tempList = new ArrayList<>();
        //태그 쪼개기
        //ex) "와이파이_수영장_고라니" => "와이파이", "수영장", "고라니"
        List<String> tagList = Arrays.asList(tag.split("_"));

        //선택한 시군구의 camp 리스트 받아오기
        List<Camp> campListFilterBySigungu = campRepository.findAllBySigunguNm(sigunguNm);

        for (Camp cam : campListFilterBySigungu) {
            if (cam.getSbrsCl() == null) {
                continue;
            }
            List<String> camTagList = new ArrayList<>(Arrays.asList(cam.getSbrsCl().split(",")));
            camTagList.retainAll(tagList);
            if(!camTagList.isEmpty()){
                tempList.add(cam);
            }
        }
        List<CampSearchResponseDto> campSearchListDto =
                tempList.stream().map(CampSearchResponseDto::new).collect(Collectors.toList());

        return campSearchListDto;
    }
}
