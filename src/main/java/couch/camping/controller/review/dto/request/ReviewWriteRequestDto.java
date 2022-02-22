package couch.camping.controller.review.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "리뷰 작성 요청 DTO")
public class ReviewWriteRequestDto {

    @ApiModelProperty(required = true, value = "리뷰 내용(최소 길이 5)", example = "여기 캠핑장 추천합니다.")
    @Length(min = 5)
    @NotBlank
    private String content;

    @ApiModelProperty(required = true, value = "평점 (1~5 사이의 정수)", example = "1~5 사이의 자연수")
    @Max(5)
    @Min(1)
    @NotNull
    private Integer rate;
    
    @ApiModelProperty(required = true, value = "이미지 url", example = "https://gocamping.or.kr/upload/camp/1023/thumb/thumb_720_8920mL8X4QwnXtiSF10BNgEW.jpg")
    private String imgUrl;
}
