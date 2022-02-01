package couch.camping.domain.camp;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CampListDto {

    List<CampDto> item = new ArrayList<>();
}
