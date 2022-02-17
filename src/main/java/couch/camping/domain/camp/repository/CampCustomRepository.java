package couch.camping.domain.camp.repository;

import couch.camping.controller.camp.dto.response.CampSearchPagingResponseDto;
import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.entity.Camp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampCustomRepository {
    Page<Camp> findAllCampSearch(List<String> tagList, String sigunguNm, Pageable pageable);

    Double calcDistance(Long campId, Float mapX, Float mapY);
}
