package io.wisoft.vamos.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@ApiModel("로그인 요청")
public class LoginRequest {

    @ApiModelProperty(
            value = "로그인 이메일",
            name = "email",
            example = "tester@gmail.com",
            required = true
    )
    @NotBlank
    @Email
    private String email;

    @ApiModelProperty(
            value = "로그인 비밀번호",
            name = "password",
            example = "password",
            required = true
    )
    @NotBlank
    private String password;
}
