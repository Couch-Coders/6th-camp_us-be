package couch.camping.controller.post.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostEditRequestDto {

    @ApiModelProperty(required = true, value = "게시글 제목", example = "질문있습니다!")
    private String title;

    @ApiModelProperty(required = true, value = "게시글 내용", example = "안녕하세요. 궁금한 게 있어서 질문드립니다. 쏼라쏼라~~")
    private String content;

    @ApiModelProperty(required = true, value = "게시글 분류", example = "free")
    private String postType;

    @ApiModelProperty(required = true, value = "게시글 업로드 된 이미지", example = "[\"www.abc.com\", \"www.avb.com\"]")
    private List<String> imgUrlList = new ArrayList<>();
}
