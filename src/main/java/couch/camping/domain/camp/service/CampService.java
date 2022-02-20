package couch.camping.domain.camp.service;

import com.querydsl.core.group.GroupBy;
import couch.camping.controller.camp.dto.response.CampResponseDto;
import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CampService {
    void save(Camp camp);
    CampResponseDto getCampDetail(Long campId, String header);
    Page<CampSearchResponseDto> getCampList(Pageable pageable, String name, String doNm, String sigunguNm, String tag, String header, String sort, Float mapX, Float mapY);
    void likeCamp(Long campId, Member member);
    Page<Camp> retrieveMemberLikeCamp(Long memberId, Pageable pageable);
}
