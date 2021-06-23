package io.wisoft.vamos.controller.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.user.Authority;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.wisoft.vamos.controller.api.ApiResult.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController {

    private final UserService userService;

    /**
     * 사용자 회원가입
     * @param request dto
     * @return user info
     */
    @PostMapping("/join")
    public ApiResult<UserResponse> join(@Valid @RequestBody UserJoinRequest request) {

        User newUser = getUser(request);
        return succeed(new UserResponse(userService.join(newUser)));
    }

    /**
     * 쿼리 파라미터를 통해 특정 사용자 조회
     * @param username
     * @return user info
     */
    @GetMapping("/user")
    public ApiResult<UserResponse> userInfo(@RequestParam String username) {
        return succeed(new UserResponse(userService.findByUsername(username)));
    }

    /**
     * 전체 사용자 조회
     * @return user infos
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResult<List<UserResponse>> allUsers() {
        return succeed(userService.findAll()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList()));
    }

    @Getter
    private static class UserJoinRequest {

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

    @Getter
    @Setter
    protected static class UserResponse {

        private Long id;

        @JsonProperty("username")
        private String username;

        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("phone_number")
        private PhoneNumber phoneNumber;

        @JsonProperty("user_roles")
        private Set<Authority> authorities;

        @JsonProperty("location")
        private UserLocation location;

        public UserResponse(User source) {
            BeanUtils.copyProperties(source, this);
        }
    }

    private User getUser(UserJoinRequest request) {

        UserLocation location = UserLocation.from(request.getX(), request.getY(), request.getAddressName());

        return User.from(
                request.getUsername(),
                request.getPassword(),
                PhoneNumber.of(request.getPhoneNumber()),
                request.getNickname(),
                location
        );
    }
}
