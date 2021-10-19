package io.wisoft.vamos.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.user.UserLocationUpdateRequest;
import io.wisoft.vamos.dto.user.UserResponse;
import io.wisoft.vamos.security.UserPrincipal;
import io.wisoft.vamos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

import static io.wisoft.vamos.dto.api.ApiResult.succeed;

@RequiredArgsConstructor
@Api(tags = "UserController")
@RestController
@RequestMapping("api")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "현재 사용자 조회", notes = "현재 로그인 되어 있는 사용자를 조회합니다.")
    @GetMapping("/user/me")
    public ApiResult<UserResponse> getCurrentUser(@ApiIgnore UserPrincipal userPrincipal) {
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
    @ApiOperation(value = "사용자 단건 조회", notes = "사용자명으로 특정 사용자를 조회합니다.")
    @ApiImplicitParam(name = "name", value = "사용자명", example = "tester", required = true, dataTypeClass = String.class, paramType = "query")
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
    @ApiOperation(value = "사용자 리스트 조회", notes = "모든 사용자들을 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
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
    @ApiOperation(value = "사용자 위치정보 수정", notes = "사용자의 위치정보를 수정합니다.")
    @PatchMapping("/user/location")
    public ApiResult<UserResponse> updateUserLocation(
            @RequestBody UserLocationUpdateRequest request,
            @ApiIgnore UserPrincipal userPrincipal) {
        return succeed(
                new UserResponse(userService.updateUserLocation(userPrincipal.getEmail(), request))
        );
    }

}
