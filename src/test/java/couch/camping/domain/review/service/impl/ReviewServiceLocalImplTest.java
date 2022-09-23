package couch.camping.domain.review.service.impl;

import couch.camping.controller.review.dto.request.ReviewWriteRequestDto;
import couch.camping.controller.review.dto.response.ReviewRetrieveLoginResponse;
import couch.camping.controller.review.dto.response.ReviewRetrieveResponseDto;
import couch.camping.controller.review.dto.response.ReviewWriteResponseDto;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.camp.CampRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.review.entity.Review;
import couch.camping.domain.review.entity.ReviewLike;
import couch.camping.domain.review.repository.review.ReviewRepository;
import couch.camping.domain.review.repository.review_like.ReviewLikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceLocalImplTest {

    @Mock
    ReviewRepository reviewRepository;
    @Mock
    ReviewLikeRepository reviewLikeRepository;
    @Mock
    CampRepository campRepository;
    @Mock
    UserDetailsService userDetailsService;

    @InjectMocks
    ReviewServiceLocalImpl reviewService;

    final String uid = "1234";
    final String email = "email.@naver.com";
    final String name = "김상운";
    final String nickname = "balladang";

    final String content = "리뷰 내용";
    private String reviewImgUrl = "이미지 url";

    @Test
    @DisplayName("캠핑장 리뷰 작성")
    public void writeTest() throws Exception {
        //given
        Member member = createMember(uid, email, name, nickname);

        Camp camp = new Camp();
        Review review = createReview(camp, member, content, reviewImgUrl);

        ReviewWriteResponseDto expected = new ReviewWriteResponseDto(review);

        when(campRepository.findById(any())).thenReturn(Optional.ofNullable(camp));
        when(reviewRepository.save(any())).thenReturn(review);

        //when
        ReviewWriteRequestDto reviewWriteRequestDto = new ReviewWriteRequestDto(content, 5, reviewImgUrl);
        ReviewWriteResponseDto result = reviewService.write(1L, member, reviewWriteRequestDto);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("로그인하지 않고 캠핑장 리뷰 전체 조회")
    public void retrieveAllWithNoLoginTest() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Member member = createMember(uid, email, name, nickname);

        Camp camp = new Camp();
        List<Review> reviewList = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            reviewList.add(createReview(camp, member, content+i, reviewImgUrl));
        }

        PageImpl<Review> reviewPage = new PageImpl<>(reviewList, pageRequest, 3L);

        List<ReviewRetrieveResponseDto> responseDtoList = new ArrayList<>();
        for (Review review : reviewList) {
            responseDtoList.add(new ReviewRetrieveResponseDto(review));
        }

        PageImpl<ReviewRetrieveResponseDto> expected = new PageImpl<>(responseDtoList, pageRequest, 3L);

        when(reviewRepository.findByCampId(any(), any())).thenReturn(reviewPage);

        //when
        Page<ReviewRetrieveResponseDto> result = reviewService.retrieveAll(1L, pageRequest, null);

        //then

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("로그인 후 캠핑장 리뷰 전체 조회")
    public void retrieveAllWithLoginTest() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Member member = createMember(uid, email, name, nickname);

        Camp camp = new Camp();
        List<Review> reviewList = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            reviewList.add(createReview(camp, member, content+i, reviewImgUrl));
        }

        PageImpl<Review> reviewPage = new PageImpl<>(reviewList, pageRequest, 3L);

        List<ReviewRetrieveResponseDto> responseDtoList = new ArrayList<>();
        for (Review review : reviewList) {
            responseDtoList.add(new ReviewRetrieveLoginResponse(review, false));
        }

        PageImpl<ReviewRetrieveResponseDto> expected = new PageImpl<>(responseDtoList, pageRequest, 3L);

        when(reviewRepository.findByCampId(any(), any())).thenReturn(reviewPage);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(member);

        //when
        Page<ReviewRetrieveResponseDto> result = reviewService.retrieveAll(1L, pageRequest, "abcd");
        //then

        assertThat(result).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("리뷰 삭제")
    public void deleteReviewTest() throws Exception {
        //given
        Member member = createMember(uid, email, name, nickname);
        Camp camp = new Camp();
        Review review = createReview(camp, member, content, reviewImgUrl);

        when(reviewRepository.findById(any())).thenReturn(Optional.ofNullable(review));
        doNothing().when(reviewRepository).deleteById(any());
        //when
        reviewService.deleteReview(1L, member);
        
        //then
        verify(reviewRepository, times(1)).deleteById(any());
    }
    
    @Test
    @DisplayName("리뷰 수정")
    public void editReviewTest() throws Exception {
        //given
        int editRate = 3;
        String editContent = content + 1;
        String editReviewImage = reviewImgUrl + 1;

        Member member = createMember(uid, email, name, nickname);
        Camp camp = new Camp();
        Review review = createReview(camp, member, content, reviewImgUrl);

        Review editReview = createReview(camp, member, editContent, editReviewImage);
        editReview.setRate(editRate);

        ReviewWriteRequestDto reviewWriteRequestDto = new ReviewWriteRequestDto(editContent, editRate, editReviewImage);

        ReviewWriteResponseDto expected = new ReviewWriteResponseDto(editReview);

        when(reviewRepository.findById(any())).thenReturn(Optional.of(review));

        //when
        ReviewWriteResponseDto result = reviewService.editReview(1L, reviewWriteRequestDto, member);

        //then
        assertThat(result).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("좋아요 한적 있는 리뷰 좋아요 테스트")
    public void neverLikedReviewTest() throws Exception {
        //given
        Member member = createMember(uid, email, name, nickname);
        Camp camp = new Camp();
        Review review = createReview(camp, member, content, reviewImgUrl, 3);
        ReviewLike reviewLike = createReview(member, review);

        when(reviewRepository.findById(any())).thenReturn(Optional.ofNullable(review));
        when(reviewLikeRepository.findByReviewIdAndMemberId(any(), any())).thenReturn(Optional.ofNullable(reviewLike));
        doNothing().when(reviewLikeRepository).deleteById(any());

        //when
        reviewService.likeReview(1L, member);

        //then
        assertThat(review.getLikeCnt()).isEqualTo(2);
        verify(reviewLikeRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("좋아요 한적 없는 리뷰 좋아요 테스트")
    public void likeReviewTest() throws Exception {
        //given
        Member member = createMember(uid, email, name, nickname);
        Camp camp = new Camp();
        Review review = createReview(camp, member, content, reviewImgUrl, 3);
        ReviewLike reviewLike = createReview(member, review);

        when(reviewRepository.findById(any())).thenReturn(Optional.ofNullable(review));
        when(reviewLikeRepository.findByReviewIdAndMemberId(any(), any())).thenReturn(Optional.empty());
        when(reviewLikeRepository.save(any())).thenReturn(reviewLike);

        //when
        reviewService.likeReview(1L, member);

        //then
        assertThat(review.getLikeCnt()).isEqualTo(4);
    }

    private ReviewLike createReview(Member member, Review review) {
        return ReviewLike.builder()
                .review(review)
                .member(member)
                .build();
    }

    private Review createReview(Camp camp, Member member, String content, String reviewImgUrl) {
        Review build = Review.builder()
                .camp(camp)
                .member(member)
                .content(content)
                .imgUrl(reviewImgUrl)
                .reviewLikeList(new ArrayList<>())
                .reviewLikeList(new ArrayList<>())
                .build();
        return build;
    }

    private Review createReview(Camp camp, Member member, String content, String reviewImgUrl, int likeCnt) {
        Review build = Review.builder()
                .likeCnt(likeCnt)
                .camp(camp)
                .member(member)
                .content(content)
                .imgUrl(reviewImgUrl)
                .reviewLikeList(new ArrayList<>())
                .reviewLikeList(new ArrayList<>())
                .build();
        return build;
    }

    private Member createMember(String uid, String email, String name, String nickname) {
        return Member
                .builder()
                .uid(uid)
                .email(email)
                .name(name)
                .nickname(nickname)
                .build();
    }

}