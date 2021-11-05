package io.wisoft.vamos.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@ApiModel("회원 가입 요청")
public class SignUpRequest {

    @ApiModelProperty(
            value = "회원가입 사용자 별명",
            name = "nickname",
            example = "tester",
            required = true
    )
    @NotBlank
    private String nickname;

    @ApiModelProperty(
            value = "회원가입 이메일",
            name = "email",
            example = "tester@gmail.com",
            required = true
    )
    @NotBlank
    @Email
    private String email;

    @ApiModelProperty(
            value = "회원가입 비밀번호",
            name = "password",
            example = "password",
            required = true
    )
    @NotBlank
    private String password;
}
