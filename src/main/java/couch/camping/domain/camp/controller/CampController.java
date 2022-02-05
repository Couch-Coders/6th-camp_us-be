package couch.camping.domain.camp.controller;

import couch.camping.domain.camp.dto.CampDto;
import couch.camping.domain.camp.dto.CampListDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.service.CampService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/camps")
public class CampController {

    private final CampService campService;
    private final ModelMapper modelMapper;

    @PostMapping("")
    public String save(@RequestBody CampListDto campListDto) {

        for (CampDto campDto : campListDto.getItem()) {
            Camp map = modelMapper.map(campDto, Camp.class);
            campService.save(map);
        }
        return "ok";
    }
}
