package io.wisoft.vamos.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@ApiModel("사용자 정보 수정 요청")
public class UserUpdateRequest {

    @ApiModelProperty(
            value = "사용자 별명",
            name = "nickname",
            example = "nickname"
    )
    @NotBlank(message = "별명은 공백일 수 없습니다.")
    private String nickname;

    @ApiModelProperty(
            value = "사용자 프로필",
            name = "picture",
            example = "[profile url]]"
    )
    private String picture;

    @ApiModelProperty(
            value = "로그인 비밀번호",
            name = "password",
            example = "password"
    )
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private String password;
}
