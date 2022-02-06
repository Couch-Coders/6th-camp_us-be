package couch.camping.controller.review.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewWriteRequestDto {

    private String content;
    private int rate;
    private String imgUrl;
}
