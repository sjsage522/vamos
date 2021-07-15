package io.wisoft.vamos.dto.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentUpdateRequest {

    @NotBlank(message = "답글 내용은 필수입니다.")
    private String content;
}
