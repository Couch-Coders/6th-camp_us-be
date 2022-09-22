package couch.camping.domain.camp.repository.camp;

import couch.camping.domain.camp.entity.Camp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampCustomRepository {
    Page<Camp> findMemberLikeCamp(Long memberId, Pageable pageable);
    Page<Camp> findAllCampBySearchCondOrderByRate(List<String> tagList, String name, String doNm, String sigunguNm, int rate, Pageable pageable);
    List<Camp> findAllByCampId(List<Long> campIds);
    Page<Camp> findAllCampBySearchCondOrderByDistanceNativeQuery(List<String> tagList, String name, Pageable pageable, Float mapX, Float mapY);
}