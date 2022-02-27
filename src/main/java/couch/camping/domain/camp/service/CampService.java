package couch.camping.domain.camp.service;

import couch.camping.controller.camp.dto.response.CampResponseDto;
import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CampService {
    void save(Camp camp);
    CampResponseDto getCampDetail(Long campId, String header);
    Page<CampSearchResponseDto> getLoginCampList(Pageable pageable, String name, String doNm, String sigunguNm, String tag, int rate, String header, String sort, Float mapX, Float mapY);
    void likeCamp(Long campId, Member member);
    Page<CampSearchResponseDto> getMemberLikeCamps(Long memberId, Pageable pageable);
    Page<CampSearchResponseDto> getCampList(Pageable pageable, String name, String doNm, String sigunguNm, String tag, int rate, String sort, Float mapX, Float mapY);

}
