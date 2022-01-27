package couch.camping.service;

import couch.camping.entity.Camp;
import couch.camping.repository.CampRepository;
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
}
