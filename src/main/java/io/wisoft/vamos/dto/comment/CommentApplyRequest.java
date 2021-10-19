package io.wisoft.vamos.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ApiModel("답글 업로드 요청")
public class CommentApplyRequest {

    @ApiModelProperty(
            value = "부모 답글 고유 id",
            name = "parentId",
            example = "1"
    )
    private Long parentId;

    @ApiModelProperty(
            value = "답글 내용",
            name = "content",
            example = "content",
            required = true
    )
    @NotBlank(message = "답글 내용은 필수입니다.")
    private String content;
}
