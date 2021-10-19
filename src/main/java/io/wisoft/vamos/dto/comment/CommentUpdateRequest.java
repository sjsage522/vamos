package io.wisoft.vamos.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ApiModel("답글 수정 요청")
public class CommentUpdateRequest {

    @ApiModelProperty(
            value = "수정할 답글 내용",
            name = "content",
            example = "content",
            required = true
    )
    @NotBlank(message = "답글 내용은 필수입니다.")
    private String content;
}
