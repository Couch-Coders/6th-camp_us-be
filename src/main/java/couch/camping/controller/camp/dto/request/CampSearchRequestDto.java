package couch.camping.controller.camp.dto.request;

import couch.camping.domain.camp.entity.Camp;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class CampSearchRequestDto {
    //?rate=4.5
    // &doNm=경상도
    // &sigunguNm=창원군
    // &name=달빛캠핑장
    // &sort1=distance
    // &sort2=like
    // &tag=와이파이_전기_하수도_수영장
    // &x=3.1423
    // &y=3.141592
    private float rate;
    private String doNm;
    private String sigunguNm;
    private String name;
    private float mapX;
    private float mapY;
}
