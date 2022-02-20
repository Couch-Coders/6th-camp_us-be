package couch.camping.controller.camp.dto.response;

import couch.camping.domain.camp.entity.Camp;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "로그인 후 리뷰 조회 응답 DTO")
public class CampLoginResponseDto extends CampResponseDto{

    private boolean isLiked;

    public CampLoginResponseDto(Camp camp, boolean isLiked) {
        super(camp);
        this.isLiked = isLiked;
    }
}
