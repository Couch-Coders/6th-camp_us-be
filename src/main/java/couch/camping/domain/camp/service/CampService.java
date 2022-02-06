package couch.camping.domain.camp.service;

import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.CampRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Camp findCamp = campRepository.findById(campId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_CAMP, "campId 에 맞는 캠핑장이 없습니다.");
                });
        return findCamp;
    }
}
