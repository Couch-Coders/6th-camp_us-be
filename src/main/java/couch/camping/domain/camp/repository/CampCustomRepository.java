package couch.camping.domain.camp.repository;

import couch.camping.domain.camp.entity.Camp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampCustomRepository {
    Page<Camp> findMemberLikeCamp(Long memberId, Pageable pageable);
    Page<Camp> findAllCampSearch(List<String> tagList, String name, String doNm, String sigunguNm, int rate, String sort, Pageable pageable, Float mapX, Float mapY);
}