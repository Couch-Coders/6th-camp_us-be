package couch.camping.domain.review.service;

import couch.camping.controller.review.dto.request.ReviewRequestModel;
import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewRetrieveResponseDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.CampRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.repository.MemberRepository;
import couch.camping.domain.review.entity.Review;
import couch.camping.domain.review.repository.ReviewRepository;
import couch.camping.exception.CustomException;
import couch.camping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CampRepository campRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewWriteResponseDto write(Long campId, Member member, ReviewWriteRequestDto reviewWriteRequestDto) {

        Camp findCamp = campRepository.findById(campId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_CAMP, "캠핑장 ID 에 해당하는 캠핑장이 없습니다.");
                });


        member.increaseReviewCnt();

        Review review = Review.builder()
                .member(member)
                .camp(findCamp)
                .imgUrl(reviewWriteRequestDto.getImgUrl())
                .content(reviewWriteRequestDto.getContent())
                .rate(reviewWriteRequestDto.getRate())
                .build();

        return new ReviewWriteResponseDto(reviewRepository.save(review));
    }

    public Page<ReviewRetrieveResponseDto> retrieveAll(Long campId, ReviewRequestModel reviewRequestModel) {
        int page = reviewRequestModel.getPage();
        int size = reviewRequestModel.getSize();
        int sort = reviewRequestModel.getSort();

        Sort.Direction sortType;
        if (sort == 0) sortType = Sort.Direction.DESC;
        else sortType = Sort.Direction.ASC;

        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by(sortType, "createdDate"));


        return reviewRepository.findByCampId(pageRequest, campId)
                .map(review -> new ReviewRetrieveResponseDto(review));
    }


    @Transactional
    public void deleteReview(Long reviewId, Member member) {

        try {
            member.decreaseReviewCnt();
            reviewRepository.deleteById(reviewId);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException(ErrorCode.NOT_FOUND_REVIEW, "리뷰 ID 에 맞는 리뷰가 없습니다.");
        }

    }

    @Transactional
    public ReviewWriteResponseDto editReview(Long reviewId, ReviewWriteRequestDto reviewWriteRequestDto, Member member) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_REVIEW, "리뷰 ID 에 맞는 리뷰가 없습니다.");
                });

        if (review.getMember().getId() != member.getId()) {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER, "해당 회원의 리뷰가 아닙니다.");
        }

        Review editReview = review.changeReview(
                reviewWriteRequestDto.getContent(),
                reviewWriteRequestDto.getRate(),
                reviewWriteRequestDto.getImgUrl()
        );

        return new ReviewWriteResponseDto(editReview);
    }
}
