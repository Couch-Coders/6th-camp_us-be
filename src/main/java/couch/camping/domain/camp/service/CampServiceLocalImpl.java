package couch.camping.domain.camp.service;

import couch.camping.controller.camp.dto.response.CampSearchLoginResponse;
import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.CampRepository;
import couch.camping.domain.camplike.entity.CampLike;
import couch.camping.domain.camplike.repository.CampLikeRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Profile("local")
public class CampServiceLocalImpl implements CampService{

    private final CampRepository campRepository;
    private final CampLikeRepository campLikeRepository;
    private final UserDetailsService userDetailsService;

    @Transactional
    public void save(Camp camp) {
        campRepository.save(camp);
    }

    //캠핑장 단건 조회
    @Override
    public Camp getCampDetail(Long campId) {
        Camp findCamp = campRepository.findById(campId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_CAMP, "campId 에 맞는 캠핑장이 없습니다.");
                });
        return findCamp;
    }

    //캠핑장 조건 다중 조회
    @Override
    public Page<CampSearchResponseDto> getCampList(
            Pageable pageable, String name, String doNm, String sigunguNm, String tag, String header, String sort, Float mapX, Float mapY) {
        List<String> tagList = new ArrayList<>();
        if (tag!= null)
            tagList = Arrays.asList(tag.split("_"));

        if (!sort.equals("distance") && !sort.equals("rate")) {
            throw new CustomException(ErrorCode.BAD_REQUEST_PARAM, "sort 의 값을 distance 또는 rate 만 입력가능합니다.");
        }

        if (header == null) {
            return campRepository.findAllCampSearch(tagList, name, doNm, sigunguNm, sort, pageable, mapX, mapY)
                    .map(camp -> new CampSearchResponseDto(camp));
        }
        else {
            Member member;
            try {
                member = (Member) userDetailsService.loadUserByUsername(header);
            } catch (UsernameNotFoundException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "토큰에 해당하는 회원이 존재하지 않습니다.");
            }
            return campRepository.findAllCampSearch(tagList, name, doNm, sigunguNm, sort, pageable, mapX, mapY)
                    .map(camp -> {
                        List<CampLike> campLikeList = camp.getCampLikeList();

                        for (CampLike campLike : campLikeList) {
                            if (campLike.getMember() != member){
                                return new CampSearchLoginResponse(camp, false);
                            }
                        }
                        return new CampSearchLoginResponse(camp, true);
                    });
        }

    }

    @Transactional
    @Override
    public void likeCamp(Long campId, Member member) {
        Camp findCamp = campRepository.findById(campId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_CAMP, "캠핑장 ID 에 맞는 캠핑장이 없습니다.");
                });

        Optional<CampLike> campLike = campLikeRepository.findByCampIdAndMemberId(campId, member.getId());

        if (campLike.isPresent()) {
            findCamp.decreaseCampLikeCnt();
            campLikeRepository.deleteById(campLike.get().getId());
        } else{
            findCamp.increaseCampLikeCnt();
            CampLike saveCampLike = campLikeRepository.save(CampLike.builder()
                    .member(member)
                    .camp(findCamp)
                    .build());

            findCamp.getCampLikeList().add(saveCampLike);
        }
    }

    @Override
    public Page<CampSearchResponseDto> getMemberLikeCamps(Long memberId, Pageable pageable) {
        return campRepository.findMemberLikeCamp(memberId, pageable)
                .map(camp -> new CampSearchResponseDto(camp));
    }
}
