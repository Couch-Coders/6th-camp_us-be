package couch.camping.domain.camp.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import couch.camping.controller.camp.dto.response.CampResponseDto;
import couch.camping.controller.camp.dto.response.CampSearchPagingResponseDto;
import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.entity.QCamp;
import couch.camping.domain.camp.repository.CampRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CampService {

    private final CampRepository campRepository;
    @Autowired
    EntityManager em;

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
    public CampSearchPagingResponseDto getCampList(
            Pageable pageable, float rate, String name, String tag) {

        List<String> tagList = Arrays.asList(tag.split("_"));
        Page<Camp> allCampSearch = campRepository.findAllCampSearch(tagList, pageable);

        List<Camp> content = allCampSearch.getContent();
        int totalElements = (int) allCampSearch.getTotalElements();
        int totalPages = allCampSearch.getTotalPages();

        List<CampSearchResponseDto> campSearchListDto =
                content.stream().map(CampSearchResponseDto::new).collect(Collectors.toList());

        CampSearchPagingResponseDto campSearchPagingResponseDto = new CampSearchPagingResponseDto(totalPages, totalElements, campSearchListDto);
        return campSearchPagingResponseDto;
    }
}
