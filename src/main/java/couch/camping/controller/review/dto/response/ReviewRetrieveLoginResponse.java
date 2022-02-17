package couch.camping.controller.review.dto.response;


import couch.camping.domain.review.entity.Review;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRetrieveLoginResponse extends ReviewRetrieveResponseDto{

    private boolean isLiked;

    public ReviewRetrieveLoginResponse(Review review, boolean isLiked) {
        super(review);
        this.isLiked = isLiked;
    }
}
