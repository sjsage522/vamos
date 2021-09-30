package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.user.UserLocationUpdateRequest;
import io.wisoft.vamos.dto.user.UserResponse;
import io.wisoft.vamos.security.UserPrincipal;
import io.wisoft.vamos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.dto.api.ApiResult.succeed;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController {

    private final UserService userService;

    @GetMapping("/user/me")
    public ApiResult<UserResponse> getCurrentUser(UserPrincipal userPrincipal) {
        return succeed(
                new UserResponse(userService.findByEmail(userPrincipal.getEmail()))
        );
    }

    /**
     * 쿼리 파라미터를 통해 특정 사용자 조회
     *
     * @param name
     * @return user info
     */
    @GetMapping("/user")
    public ApiResult<UserResponse> getUser(@RequestParam("name") String name) {
        return succeed(
                new UserResponse(userService.findByUsername(name))
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

    /**
     * 사용자 위치정보 등록 및 수정
     *
     * @param request dto
     * @return user info
     */
    @PatchMapping("/user/location")
    public ApiResult<UserResponse> userLocationUpdate(
            @RequestBody UserLocationUpdateRequest request,
            UserPrincipal userPrincipal) {
        return succeed(
                new UserResponse(userService.updateUserLocation(userPrincipal.getEmail(), request))
        );
    }

}
