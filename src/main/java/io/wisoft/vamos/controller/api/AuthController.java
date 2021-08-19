package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.config.auth.LoginUser;
import io.wisoft.vamos.config.auth.dto.SessionUser;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.service.SmsCertificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

import static io.wisoft.vamos.dto.api.ApiResult.succeed;

/**
 * TODO 로그인 방식 OAuth 를 이용하도록 변경.. 문자인증은 남겨두고 JWT는 삭제.. 레디스도 삭제..
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final SmsCertificationService smsCertificationService;

    /**
     * 문자 메시지 인증번호를 얻기위한 요청
     * @param request 사용자 핸드폰 번호가 포함된 요청
     */
    @PostMapping("/sms-certification/sends")
    public ApiResult<SmsResponse> sendSms(@RequestBody SmsPhoneNumberRequest request, @LoginUser SessionUser sessionUser) {
        String certification = smsCertificationService.sendSms(PhoneNumber.of(request.getPhoneNumber()));
        System.out.println("sessionUser = " + sessionUser);
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

        private String content;
        private String certification;

        public SmsResponse(String content, String certification) {
            this.content = content;
            this.certification = certification;
        }
    }
}
