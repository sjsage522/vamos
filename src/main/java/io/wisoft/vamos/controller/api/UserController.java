package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.user.UserLocationUpdateRequest;
import io.wisoft.vamos.dto.user.UserResponse;
import io.wisoft.vamos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.dto.api.ApiResult.succeed;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController {

    private final UserService userService;

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
}
