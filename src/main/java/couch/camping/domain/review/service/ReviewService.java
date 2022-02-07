package couch.camping.domain.review.service;

import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.CampRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.repository.MemberRepository;
import couch.camping.domain.review.entity.Review;
import couch.camping.domain.review.repository.ReviewRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CampRepository campRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Review write(Long campId, Long memberId, ReviewWriteRequestDto reviewWriteRequestDto) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_USER, "memberId 에 맞는 회원이 없습니다.");
                });

        Camp findCamp = campRepository.findById(campId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_CAMP, "campId 에 맞는 캠핑장이 없습니다.");
                });


        findMember.setReviewCnt(findMember.getReviewCnt()+1);

        Review review = Review.builder()
                .member(findMember)
                .camp(findCamp)
                .imgUrl(reviewWriteRequestDto.getImgUrl())
                .content(reviewWriteRequestDto.getContent())
                .rate(reviewWriteRequestDto.getRate())
                .build();

        return reviewRepository.save(review);
    }

    public List<Review> retrieveAll(Long campId) {
        return reviewRepository.findByCampId(campId);
    }

}
