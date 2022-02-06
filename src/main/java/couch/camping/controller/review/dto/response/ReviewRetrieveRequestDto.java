package couch.camping.controller.review.dto.response;


import couch.camping.domain.review.entity.Review;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRetrieveRequestDto {

    private Long reviewId;
    private Long memberId;
    private Long campId;
    private String imgUrl;
    private String content;
    private int rate;
    private int like;

    public List<ReviewRetrieveRequestDto> listMapper(List<Review> reviews) {
        List<ReviewRetrieveRequestDto> list = new ArrayList<>();
        for (Review r : reviews) {
            list.add(new ReviewRetrieveRequestDto(
                    r.getId(),
                    r.getMember().getId(),
                    r.getCamp().getId(),
                    r.getImgUrl(),
                    r.getContent(),
                    r.getRate(),
                    r.getLike()
            ));
        }
        return list;
    }
}
