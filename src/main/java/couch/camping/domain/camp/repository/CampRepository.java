package couch.camping.domain.camp.repository;

import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.entity.Camp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampRepository extends JpaRepository<Camp, Long>, CampCustomRepository{
    Page<Camp> findAllBySigunguNm(String sigunguNm, Pageable pageable);

    Page<CampSearchResponseDto> findByRate(float rate, Pageable pageable);
}
