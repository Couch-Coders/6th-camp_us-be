package couch.camping.controller.post.dto.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class PostWriteRequestDto {

    private String content;

    private String postType;

    private List<String> imgUrlList = new ArrayList<>();

}
