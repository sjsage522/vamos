package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.jwt.TokenProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static io.wisoft.vamos.controller.api.ApiResult.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final StringRedisTemplate redisTemplate;

    @PostMapping("/login")
    public ApiResult<TokenDto> login(@Valid @RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        return succeed(new TokenDto(jwt));
    }

    /**
     * redis 를 이용한 logout 처리
     * @param request .
     */
    @PostMapping("/logout")
    public ApiResult<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").split(" ")[1];
        ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
        logoutValueOperations.set(token, token);
//        User principal = (User) tokenProvider.getAuthentication(jwt).getPrincipal();
//        System.out.println(principal.getUsername());
        return succeed(ResponseEntity.ok("로그아웃 되었습니다."));
    }

    @Getter
    private static class TokenDto {

        private final String token;

        public TokenDto(String token) {
            this.token = token;
        }
    }

    @Getter
    private static class LoginRequest {

        @NotBlank(message = "사용자 아이디를 입력해 주세요.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        private String password;
    }
}
