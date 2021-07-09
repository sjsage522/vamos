package io.wisoft.vamos.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserJoinRequest {

    @NotBlank(message = "사용자 아이디를 입력해 주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

    @NotBlank(message = "별명을 입력해 주세요.")
    private String nickname;

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String phoneNumber;

    @NotNull(message = "x 좌표값을 입력해 주세요.")
    private Double x;
    @NotNull(message = "y 좌표값을 입력해 주세요.")
    private Double y;

    @NotBlank(message = "지역 명칭을 입력해 주세요.")
    private String addressName;
}
