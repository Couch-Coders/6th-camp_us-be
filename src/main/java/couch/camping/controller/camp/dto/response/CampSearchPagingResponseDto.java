package couch.camping.controller.camp.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampSearchPagingResponseDto {

    private int totalPages;

    private int totalElements;

    List<CampSearchResponseDto> contents;
}
