package couch.camping.controller;

import couch.camping.dto.CampDto;
import couch.camping.dto.CampListDto;
import couch.camping.entity.Camp;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import couch.camping.service.CampService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
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
