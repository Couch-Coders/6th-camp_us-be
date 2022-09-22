package couch.camping.domain.review.repository.review;

import couch.camping.common.BaseControllerTest;
import couch.camping.domain.camp.entity.Camp;
import couch.camping.domain.camp.repository.camp.CampRepository;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.member.repository.MemberRepository;
import couch.camping.domain.review.entity.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewCustomRepositoryImplTest extends BaseControllerTest {
    
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CampRepository campRepository;
    @Autowired
    ReviewRepository reviewRepository;
    
    final String uid = "1234";
    final String email = "email.@naver.com";
    final String name = "김상운";
    final String nickname = "balladang";

    final String content = "리뷰 내용";

    @Test
    @DisplayName("리뷰 좋아요가 1 이상인 리뷰를 좋아요 순으로 정렬")
    public void findAllReviewGoeOneOrderByLikeCnt() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);

        Camp saveCamp = campRepository.save(campRepository.save(new Camp()));
        Member saveMember = memberRepository.save(createMember(uid, email, name, nickname));

        Review saveReview1 = reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지1", 1));
        Review saveReview2 = reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지2", 3));
        Review saveReview3 = reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지3", 2));

        List<Review> reviewList = Arrays.asList(saveReview2, saveReview3, saveReview1);

        PageImpl<Review> expected = new PageImpl<>(reviewList, pageRequest, 3L);

        //when
        Page<Review> result = reviewRepository.findAllReviewGoeOneOrderByLikeCnt(pageRequest);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("캠핑장의 리뷰 조회")
    public void findByCampIdTest() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);

        Camp saveCamp = campRepository.save(campRepository.save(new Camp()));
        Member saveMember = memberRepository.save(createMember(uid, email, name, nickname));

        Review saveReview1 = reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지1", 1));
        Review saveReview2 = reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지2", 2));
        Review saveReview3 = reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지3", 3));

        List<Review> reviewList = Arrays.asList(saveReview3, saveReview2, saveReview1);
        PageImpl<Review> expected = new PageImpl<>(reviewList, pageRequest, 3L);

        //when
        Page<Review> result = reviewRepository.findByCampId(pageRequest, saveCamp.getId());

        //then
        assertThat(result).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("회원의 리뷰 조회")
    public void findByMemberIdTest() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);

        Camp saveCamp = campRepository.save(campRepository.save(new Camp()));
        Member saveMember = memberRepository.save(createMember(uid, email, name, nickname));

        Review saveReview1 = reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지1", 1));
        Review saveReview2 = reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지2", 2));
        Review saveReview3 = reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지3", 3));

        List<Review> reviewList = Arrays.asList(saveReview3, saveReview2, saveReview1);
        PageImpl<Review> expected = new PageImpl<>(reviewList, pageRequest, 3L);
        
        //when
        Page<Review> result = reviewRepository.findByMemberId(pageRequest, saveMember.getId());

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("회원이 작성한 리뷰 개수 조회")
    public void countByMemberIdTest() throws Exception {
        //given
        Camp saveCamp = campRepository.save(campRepository.save(new Camp()));
        Member saveMember = memberRepository.save(createMember(uid, email, name, nickname));

        reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지1", 1));
        reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지2", 2));
        reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지3", 3));

        Long expected = 3L;
        //when
        Long result = reviewRepository.countByMemberId(saveMember.getId());

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("캠핑장의 리뷰 이미지가 있는 리뷰 조회")
    public void findNotNullImgUrlByCampId() throws Exception {
        //given
        Camp saveCamp = campRepository.save(campRepository.save(new Camp()));
        Member saveMember = memberRepository.save(createMember(uid, email, name, nickname));

        Review saveReview1 = reviewRepository.save(createReview(saveCamp, saveMember, content, 1));
        Review saveReview2 = reviewRepository.save(createReview(saveCamp, saveMember, content,2));
        Review saveReview3 = reviewRepository.save(createReview(saveCamp, saveMember, content, "리뷰 이미지3", 3));

        List<Review> expected = Arrays.asList(saveReview3);

        //when
        List<Review> result = reviewRepository.findNotNullImgUrlByCampId(saveCamp.getId());

        //then
        assertThat(result).isEqualTo(expected);
    }

    private Review createReview(Camp camp, Member member, String content, int likeCnt) {
        Review build = Review.builder()
                .camp(camp)
                .member(member)
                .content(content)
                .likeCnt(likeCnt)
                .build();
        return build;
    }

    private Review createReview(Camp camp, Member member, String content, String reviewImgUrl, int likeCnt) {
        Review build = Review.builder()
                .camp(camp)
                .member(member)
                .content(content)
                .imgUrl(reviewImgUrl)
                .likeCnt(likeCnt)
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