package couch.camping.domain.camp.service;

import couch.camping.controller.camp.dto.response.CampResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.CampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CampService {

    private final CampRepository campRepository;

    @Transactional
    public void save(Camp camp) {
        campRepository.save(camp);
    }


    public Camp getCampDetail(Long campId) {
        Camp findCamp = campRepository.findByCampId(campId);
        return findCamp;
    }
}
