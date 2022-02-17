package couch.camping.controller.camp.dto.response;

import couch.camping.domain.camp.entity.Camp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampSearchLoginResponse extends CampSearchResponseDto{

    private boolean isLiked;

    public CampSearchLoginResponse(Camp camp, boolean isLiked) {
        super(camp);
        this.isLiked = isLiked;
    }
}
