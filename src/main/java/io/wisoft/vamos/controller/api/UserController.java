package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.user.UserLocationUpdateRequest;
import io.wisoft.vamos.dto.user.UserResponse;
import io.wisoft.vamos.exception.NoMatchUserInfoException;
import io.wisoft.vamos.security.CurrentUser;
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
    @PreAuthorize("hasRole('USER')")
    public ApiResult<UserResponse> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return succeed(
                new UserResponse(userService.findByEmail(userPrincipal.getEmail()))
        );
    }

    /**
     * 쿼리 파라미터를 통해 특정 사용자 조회
     *
     * @param email
     * @return user info
     */
    @GetMapping("/user")
    public ApiResult<UserResponse> getUser(@RequestParam String email) {
        return succeed(
                new UserResponse(userService.findByEmail(email))
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
     * @param email
     * @param request dto
     * @return user info
     */
    @PatchMapping("/user/{email}/location")
    public ApiResult<UserResponse> userLocationUpdate(
            @PathVariable String email,
            @RequestBody UserLocationUpdateRequest request,
            @CurrentUser UserPrincipal userPrincipal) {
        checkUser(email, userPrincipal);
        return succeed(
                new UserResponse(userService.updateUserLocation(email, request))
        );
    }

    private void checkUser(@PathVariable String email, UserPrincipal userPrincipal) {
        if (!email.equals(userPrincipal.getEmail())) throw new NoMatchUserInfoException();
    }
}
