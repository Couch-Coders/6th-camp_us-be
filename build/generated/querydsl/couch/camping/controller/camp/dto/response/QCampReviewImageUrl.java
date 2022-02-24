package couch.camping.controller.camp.dto.response;

import com.querydsl.core.types.ConstructorExpression;
import couch.camping.controller.review.dto.response.ReviewImageUrlResponseDto;

import javax.annotation.processing.Generated;

/**
 * couch.camping.controller.camp.dto.response.QCampReviewImageUrl is a Querydsl Projection type for CampReviewImageUrl
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCampReviewImageUrl extends ConstructorExpression<ReviewImageUrlResponseDto> {

    private static final long serialVersionUID = -662543101L;

    public QCampReviewImageUrl(com.querydsl.core.types.Expression<String> imgUrl) {
        super(ReviewImageUrlResponseDto.class, new Class<?>[]{String.class}, imgUrl);
    }

}

