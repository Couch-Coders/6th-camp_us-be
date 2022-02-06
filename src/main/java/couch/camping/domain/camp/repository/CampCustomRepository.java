package couch.camping.domain.camp.repository;

import couch.camping.controller.camp.dto.response.CampResponseDto;
import couch.camping.domain.camp.entity.Camp;

public interface CampCustomRepository {

    Camp findByCampId(Long loginId);

}
