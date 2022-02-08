package couch.camping.controller.member.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberReviewRequestDto {

    @Min(value = 1, message = "페이지를 1 이상 입력해주세요.")
    int page;

    @Min(value = 1, message = "사이즈를 1 이상 입력해주세요.")
    int size;

    @Range(min = 0, max = 1, message = "날짜순으로 내림차순은 0, 오름차순은 1 을 입력해주세요")
    int sort;

}
