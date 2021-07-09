package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.common.jwt.TokenProvider;
import io.wisoft.vamos.dto.ApiResult;
import io.wisoft.vamos.service.SmsCertificationService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
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

import java.time.Duration;

import static io.wisoft.vamos.dto.ApiResult.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final StringRedisTemplate redisTemplate;
    private final SmsCertificationService smsCertificationService;
    private final long tokenValidityInSeconds;

    public AuthController(
            TokenProvider tokenProvider,
            AuthenticationManagerBuilder authenticationManagerBuilder,
            StringRedisTemplate redisTemplate,
            SmsCertificationService smsCertificationService,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.redisTemplate = redisTemplate;
        this.smsCertificationService = smsCertificationService;
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }

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
     */
    @PostMapping("/logout")
    public ApiResult<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").split(" ")[1];
        ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
        logoutValueOperations.set(token, token, Duration.ofSeconds(tokenValidityInSeconds));
        return succeed(ResponseEntity.ok("로그아웃 되었습니다."));
    }

    /**
     * 문자 메시지 인증번호를 얻기위한 요청
     * @param request 사용자 핸드폰 번호가 포함된 요청
     */
    @PostMapping("/sms-certification/sends")
    public ApiResult<SmsResponse> sendSms(@RequestBody SmsPhoneNumberRequest request) {
        String certification = smsCertificationService.sendSms(PhoneNumber.of(request.getPhoneNumber()));
        return succeed(new SmsResponse("인증번호를 성공적으로 요청했습니다.", certification));
    }

    /**
     * 인증번호 검증 요청
     */
    @PostMapping("/sms-certification/confirms")
    public ApiResult<?> SmsVerification(@RequestBody SmsCertificationRequest request) {
        smsCertificationService.verifySms(request.getFrom(), request.getCertification());
        return succeed(ResponseEntity.ok("인증에 성공했습니다."));
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

    @Getter
    private static class SmsPhoneNumberRequest {

        @NotBlank(message = "전화번호를 입력해 주세요.")
        private String phoneNumber;
    }

    @Getter
    private static class SmsCertificationRequest {

        private String from; /* phoneNumber */

        @NotBlank(message = "인증번호를 입력해 주세요.")
        private String certification;
    }

    @Getter
    private static class SmsResponse {

        private String content;
        private String certification;

        public SmsResponse(String content, String certification) {
            this.content = content;
            this.certification = certification;
        }
    }
}
