package io.wisoft.vamos.controller.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static io.wisoft.vamos.controller.api.ApiResult.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ApiResult<UserResponse> join(@Valid @RequestBody UserJoinRequest request) {
        return succeed(
                new UserResponse(
                        userService.
                                join(PhoneNumber.of(request.getPhoneNumber()), request.getNickName())
                )
        );
    }

    @Getter
    protected static class UserJoinRequest {

        @NotBlank(message = "전화번호를 입력해 주세요.")
        private String phoneNumber;

        @NotBlank(message = "별명을 입력해 주세요.")
        private String nickName;

        private UserJoinRequest() {}
        protected UserJoinRequest(String phoneNumber, String nickName) {
            this.phoneNumber = phoneNumber;
            this.nickName = nickName;
        }

        public static UserJoinRequest from(String phoneNumber, String nickName) {
            return new UserJoinRequest(phoneNumber, nickName);
        }
    }

    @Getter
    @Setter
    private static class UserResponse {

        private Long id;

        @JsonProperty("phone_number")
        private PhoneNumber phoneNumber;

        @JsonProperty("nick_name")
        private String nickName;

        public UserResponse(User source) {
            BeanUtils.copyProperties(source, this);
        }
    }
}
