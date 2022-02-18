package couch.camping.controller.camp.dto.response;

import couch.camping.domain.camp.entity.Camp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "로그인 후 캠핑장 검색 응답 DTO")
public class CampSearchLoginResponse extends CampSearchResponseDto{

    @ApiModelProperty(required = true, value = "좋아요를 눌렀으면 true", example = "true")
    private boolean isLiked;

    public CampSearchLoginResponse(Camp camp, boolean isLiked) {
        super(camp);
        this.isLiked = isLiked;
    }
}
