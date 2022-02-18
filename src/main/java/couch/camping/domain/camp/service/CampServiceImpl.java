package couch.camping.domain.camp.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import couch.camping.controller.camp.dto.response.CampSearchLoginResponse;
import couch.camping.controller.camp.dto.response.CampSearchResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.CampRepository;
import couch.camping.domain.camplike.entity.CampLike;
import couch.camping.domain.camplike.repository.CampLikeRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.review.repository.ReviewRepository;
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
    private final ReviewRepository reviewRepository;
    private final UserDetailsService userDetailsService;
    private final FirebaseAuth firebaseAuth;

    @Transactional
    @Override
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
            Pageable pageable, String name, String sigunguNm, String tag, String header, String sort, Float mapX, Float mapY) {

        List<String> tagList = Arrays.asList(tag.split("_"));

        if (!(sort.equals("distance") || sort.equals("rate"))) {
            throw new CustomException(ErrorCode.BAD_REQUEST_PARAM, "sort 의 값을 distance 또는 rate 만 입력가능합니다.");
        }

        if (header == null) {
            return campRepository.findAllCampSearch(tagList, sigunguNm, sort, pageable, mapX, mapY)
                    .map(CampSearchResponseDto::new);
        }
        else {
            Member member;
            try {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(header);
                member = (Member)userDetailsService.loadUserByUsername(decodedToken.getUid());
            } catch (UsernameNotFoundException | FirebaseAuthException | IllegalArgumentException e) {
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, "토큰에 해당하는 회원이 존재하지 않습니다.");
            }
            return campRepository.findAllCampSearch(tagList, sigunguNm, sort, pageable, mapX, mapY)
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
}
