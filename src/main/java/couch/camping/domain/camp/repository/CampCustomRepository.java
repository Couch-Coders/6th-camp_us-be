package couch.camping.domain.camp.repository;

import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camplike.entity.CampLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampCustomRepository {
    Page<Camp> findAllCampSearch(List<String> tagList, String name, String doNm, String sigunguNm, String sort, Pageable pageable, Float mapX, Float mapY);
    Page<Camp> findByMemberId(Long memberId, List<CampLike> campLikeList, Pageable pageable);
}
