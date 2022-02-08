package couch.camping.controller.review.dto.response;


import couch.camping.domain.review.entity.Review;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRetrieveResponseDto {

    private Long reviewId;
    private Long memberId;
    private Long campId;
    private String imgUrl;
    private String content;
    private int rate;
    private int like;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public List<ReviewRetrieveResponseDto> listMapper(List<Review> reviews) {
        List<ReviewRetrieveResponseDto> list = new ArrayList<>();
        for (Review r : reviews) {
            list.add(new ReviewRetrieveResponseDto(
                    r.getId(),
                    r.getMember().getId(),
                    r.getCamp().getId(),
                    r.getImgUrl(),
                    r.getContent(),
                    r.getRate(),
                    r.getLikeCnt(),
                    r.getCreatedDate(),
                    r.getLastModifiedDate()
            ));
        }
        return list;
    }
}
