package couch.camping.controller.camp;

import couch.camping.controller.camp.dto.CampDto;
import couch.camping.controller.camp.dto.CampListDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.service.CampService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CampController {

    private final CampService campService;
    private final ModelMapper modelMapper;

    @PostMapping("/save")
    public String save(@RequestBody CampListDto campListDto) {

        for (CampDto campDto : campListDto.getItem()) {
            Camp map = modelMapper.map(campDto, Camp.class);
            campService.save(map);
        }
        return "ok";
    }
}
