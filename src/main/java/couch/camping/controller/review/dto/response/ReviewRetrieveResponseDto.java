package couch.camping.controller.review.dto.response;


import couch.camping.domain.review.entity.Review;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "리뷰 조회 응답 DTO")
public class ReviewRetrieveResponseDto {

    @ApiModelProperty(required = true, value = "리뷰 ID", example = "10")
    private Long reviewId;
    @ApiModelProperty(required = true, value = "회원 ID", example = "3")
    private Long memberId;
    @ApiModelProperty(required = true, value = "캠핑장 ID", example = "1")
    private Long campId;
    @ApiModelProperty(required = true, value = "이미지 ID", example = "https://gocamping.or.kr/upload/camp/1023/thumb/thumb_720_8920mL8X4QwnXtiSF10BNgEW.jpg")
    private String imgUrl;
    @ApiModelProperty(required = true, value = "리뷰 내용", example = "여기 캠핑장 추천합니다.")
    private String content;
    @ApiModelProperty(required = true, value = "리뷰 평점", example = "4")
    private int rate;
    @ApiModelProperty(required = true, value = "리뷰 좋아요 개수", example = "10")
    private int likeCnt;
    @ApiModelProperty(required = true, value = "회원 닉네임", example = "test")
    private String nickname;
    @ApiModelProperty(required = true, value = "캠핑장 이름", example = "달빛캠핑장")
    private String facltNm;
    @ApiModelProperty(required = true, value = "작성 날짜", example = "2022-02-18T18:26:23.9592352")
    private LocalDateTime createdDate;
    @ApiModelProperty(required = true, value = "수정 날짜", example = "2022-02-19T18:26:23.9592352")
    private LocalDateTime lastModifiedDate;

    public ReviewRetrieveResponseDto(Review r) {
        this.reviewId = r.getId();
        this.memberId = r.getMember().getId();
        this.campId = r.getCamp().getId();
        this.imgUrl= r.getImgUrl();
        this.content = r.getContent();
        this.rate = r.getRate();
        this.likeCnt =r.getLikeCnt();
        this.nickname = r.getMember().getNickname();
        this.facltNm = r.getCamp().getFacltNm();
        this.createdDate = r.getCreatedDate();
        this.lastModifiedDate = r.getLastModifiedDate();
    }
}
