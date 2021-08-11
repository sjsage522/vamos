package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.dto.ApiResult;
import io.wisoft.vamos.dto.user.UserJoinRequest;
import io.wisoft.vamos.dto.user.UserLocationUpdateRequest;
import io.wisoft.vamos.dto.user.UserResponse;
import io.wisoft.vamos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.dto.ApiResult.succeed;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController {

    private final UserService userService;

    /**
     * 사용자 회원가입
     *
     * @param request dto
     * @return user info
     */
    @PostMapping("/join")
    public ApiResult<UserResponse> userJoin(@Valid @RequestBody UserJoinRequest request) {
        return succeed(
                new UserResponse(userService.join(request))
        );
    }

    /**
     * 쿼리 파라미터를 통해 특정 사용자 조회
     *
     * @param username
     * @return user info
     */
    @GetMapping("/user")
    public ApiResult<UserResponse> getUser(@RequestParam String username) {
        return succeed(
                new UserResponse(userService.findByUsername(username))
        );
    }

    /**
     * 전체 사용자 조회
     *
     * @return user infos
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResult<List<UserResponse>> getUserList() {
        return succeed(userService.findAll()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList()));
    }

    @PatchMapping("/user/{username}/location")
    public ApiResult<UserResponse> userLocationUpdate(
            @PathVariable String username,
            @RequestBody UserLocationUpdateRequest request) {
        return succeed(
                new UserResponse(userService.updateUserLocation(username, request))
        );
    }


    @DeleteMapping("/user/{username}")
    public ApiResult<String> userDelete(@PathVariable String username) {
        userService.delete(username);
        return succeed("user is deleted successfully");
    }
}
