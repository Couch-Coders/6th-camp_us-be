package couch.camping.domain.camp.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.camp.dto.response.CampLoginResponseDto;
import couch.camping.controller.camp.dto.response.CampResponseDto;
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
import org.springframework.data.domain.PageImpl;
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
@Profile("prod")
public class CampServiceImpl implements CampService{

    private final CampRepository campRepository;
    private final CampLikeRepository campLikeRepository;
    private final UserDetailsService userDetailsService;
    private final FirebaseAuth firebaseAuth;

    @Transactional
    @Override
    public void save(Camp camp) {
        campRepository.save(camp);
    }

    //캠핑장 단건 조회
    @Override
    public CampResponseDto getCampDetail(Long campId, String header) {

        if (header == null) {
            Camp campById = campRepository.findCampById(campId);
            return new CampResponseDto(campById);
        } else {
            Member member;
            try {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(header);
                member = (Member)userDetailsService.loadUserByUsername(decodedToken.getUid());
            } catch (UsernameNotFoundException | FirebaseAuthException | IllegalArgumentException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "토큰에 해당하는 회원이 존재하지 않습니다.");
            }
            Camp campById = campRepository.findCampById(campId);
            List<CampLike> campLikeList = campById.getCampLikeList();
            for (CampLike campLike : campLikeList) {
                if (campLike.getMember() == member) {
                    return new CampLoginResponseDto(campById, true);
                }
            }
            return new CampLoginResponseDto(campById, false);
        }
    }

    //캠핑장 조건 다중 조회
    @Transactional
    @Override
    public Page<CampSearchResponseDto> getCampList(
            Pageable pageable, String name, String doNm, String sigunguNm, String tag, int rate, String header, String sort, Float mapX, Float mapY) {
        List<String> tagList = getTagList(tag);
        ValidateSortCondition(sort);

        if (header == null) {
            return campRepository.findAllCampSearch(tagList, name, doNm, sigunguNm, rate, sort, pageable, mapX, mapY)
                    .map(camp -> new CampSearchResponseDto(camp));
        } else {
            Member member;
            try {
                member = (Member) userDetailsService.loadUserByUsername(header);
            } catch (UsernameNotFoundException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "토큰에 해당하는 회원이 존재하지 않습니다.");
            }

            Page<Camp> page = campRepository.findAllCampSearch(tagList, name, doNm, sigunguNm, rate, sort, pageable, mapX, mapY);
            if (sort.equals("distance")) {
                List<Long> campIds = new ArrayList<>();
                for (Camp c : page.getContent()) {
                    campIds.add(c.getId());
                }

                List<Camp> campList = campRepository.findAllByCampId(campIds);
                List<CampSearchLoginResponse> dtoList = campToCampLoginResponseDto(member, campList);
                return new PageImpl(dtoList, pageable, page.getTotalElements());

            } else {
                return page.map(camp -> {
                    List<CampLike> campLikeList = camp.getCampLikeList();

                    for (CampLike campLike : campLikeList) {
                        if (campLike.getMember() == member){
                            return new CampSearchLoginResponse(camp, true);
                        }
                    }
                    return new CampSearchLoginResponse(camp, false);
                });
            }
        }

    }

    private void ValidateSortCondition(String sort) {
        if (!sort.equals("distance") && !sort.equals("rate")) {
            throw new CustomException(ErrorCode.BAD_REQUEST_PARAM, "sort 의 값을 distance 또는 rate 만 입력가능합니다.");
        }
    }

    private List<String> getTagList(String tag) {
        List<String> tagList = new ArrayList<>();
        if (tag != null)
            tagList = Arrays.asList(tag.split("_"));
        return tagList;
    }

    private List<CampSearchLoginResponse> campToCampLoginResponseDto(Member member, List<Camp> campList) {
        List<CampSearchLoginResponse> list = new ArrayList<>();

        for (Camp camp : campList) {
            boolean flag = false;
            for (CampLike campLike : camp.getCampLikeList()) {
                if (campLike.getMember() == member){
                    flag = true;
                    break;
                }
            }
            if (flag) list.add(new CampSearchLoginResponse(camp, true));
            else list.add(new CampSearchLoginResponse(camp, false));
        }
        return list;
    }

    //캠핑장 좋아요
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
