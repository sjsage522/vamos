package io.wisoft.vamos.controller.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.user.Authority;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.wisoft.vamos.controller.api.ApiResult.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ApiResult<UserResponse> join(@Valid @RequestBody UserJoinRequest request) {

        User newUser = getUser(request);
        return succeed(new UserResponse(userService.join(newUser)));
    }

    @GetMapping("/user")
    public ApiResult<UserResponse> userInfo(@RequestParam String username) {
        return succeed(new UserResponse(userService.findByUsername(username)));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResult<List<UserResponse>> allUsers() {
        return succeed(userService.findAll()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList()));
    }

    @Getter
    protected static class UserJoinRequest {

        @NotBlank(message = "사용자 아이디를 입력해 주세요.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        private String password;

        @NotBlank(message = "별명을 입력해 주세요.")
        private String nickname;

        @NotBlank(message = "전화번호를 입력해 주세요.")
        private String phoneNumber;
    }

    @Getter
    @Setter
    private static class UserResponse {

        private Long id;

        @JsonProperty("username")
        private String username;

        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("phone_number")
        private PhoneNumber phoneNumber;

        @JsonProperty("user_roles")
        private Set<Authority> authorities;

        public UserResponse(User source) {
            BeanUtils.copyProperties(source, this);
        }
    }

    private User getUser(UserJoinRequest request) {
        return User.from(
                request.getUsername(),
                request.getPassword(),
                PhoneNumber.of(request.getPhoneNumber()),
                request.getNickname()
        );
    }
}
