package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.Role;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.user.AuthResponse;
import io.wisoft.vamos.dto.user.LoginRequest;
import io.wisoft.vamos.dto.user.SignUpRequest;
import io.wisoft.vamos.repository.UserRepository;
import io.wisoft.vamos.security.TokenProvider;
import io.wisoft.vamos.security.oauth2.AuthProvider;
import io.wisoft.vamos.service.SmsCertificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.net.URI;

import static io.wisoft.vamos.dto.api.ApiResult.succeed;

/**
 * TODO
 * 문자인증 후 인가 로직 고민
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final SmsCertificationService smsCertificationService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        String token = tokenProvider.createToken(authentication);
        ResponseCookie responseCookie = tokenProvider.createTokenCookie(token);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> logout() {
        ResponseCookie responseCookie = tokenProvider.createTokenCookie("");
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Email address already in use.");
        }

        // Creating user's account
        User user = User.builder()
                .username(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .provider(AuthProvider.local)
                .role(Role.USER)
                .build();

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(succeed("User registered successfully"));
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

        private final String content;
        private final String certification;

        public SmsResponse(String content, String certification) {
            this.content = content;
            this.certification = certification;
        }
    }
}
