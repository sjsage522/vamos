package io.wisoft.vamos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.vamos.config.property.NaverSmsProperty;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.repository.SmsCertificationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.*;
import static org.apache.commons.lang3.RandomStringUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsCertificationService {

    private final SmsCertificationRepository smsCertificationRepository;
    private final NaverSmsProperty naverSmsProperty;

    @Transactional
    public String sendSms(PhoneNumber phone) {

        String currentTime = Long.toString(System.currentTimeMillis());
        String certification = generateRandomNumber();

        try {
            sending(phone, certification, currentTime);
        } catch (JsonProcessingException | URISyntaxException e) {
            throw new IllegalStateException();
        }

        /* 발송 정보를 redis 에 저장 */
        smsCertificationRepository.createSmsCertification(phone.getPhoneNumber(), certification);

        return certification;
    }

    @Transactional
    public void verifySms(String phoneNumber, String certification) {
        if (isValid(phoneNumber, certification)) {
            smsCertificationRepository.removeSmsCertification(phoneNumber);
        } else {
            throw new IllegalStateException("인증번호가 일치하지 않습니다.");
        }
    }

    private boolean isValid(String phoneNumber, String certification) {
        return smsCertificationRepository.hasKey(phoneNumber)
                && smsCertificationRepository.getSmsCertification(phoneNumber).equals(certification);
    }

    private void sending(PhoneNumber phone, String certification, String currentTime) throws JsonProcessingException, URISyntaxException {
        log.info("number = {}", phone.getPhoneNumber());
        HttpHeaders httpHeaders = new HttpHeaders();
        settingHeaders(httpHeaders, currentTime);

        String personalMessage = "[VAMOS] 인증번호 [" + certification + "] 를 입력해주세요.";

        List<MessagesRequest> messages = new ArrayList<>();
        messages.add(new MessagesRequest(phone.getPhoneNumber(), personalMessage));

        SmsRequest smsRequest = new SmsRequest(
                "SMS",
                naverSmsProperty.getFrom(),
                personalMessage,
                messages
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(smsRequest);
        log.info("jsonBody : {}", json);

        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        log.info("요청 http : {}", httpEntity);

        RestTemplate restTemplate = new RestTemplate();
        SmsResponse smsResponse = restTemplate.postForObject(
                new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + naverSmsProperty.getServiceId() + "/messages"),
                httpEntity,
                SmsResponse.class
        );
        log.info("응답 body : {}", smsResponse);
    }

    private void settingHeaders(HttpHeaders httpHeaders, String currentTime) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("x-ncp-apigw-timestamp", currentTime);
        httpHeaders.set("x-ncp-iam-access-key", naverSmsProperty.getAccessKey());
        httpHeaders.set("x-ncp-apigw-signature-v2", generateSignature(currentTime));
    }

    private String generateRandomNumber() {
        return randomNumeric(6);
    }

    private String generateSignature(String currentTime) {
        try {
            String space = " ";
            String newLine = "\n";
            String method = "POST";
            String url = "/sms/v2/services/" + naverSmsProperty.getServiceId() + "/messages";
            String accessKey = naverSmsProperty.getAccessKey();
            String secretKey = naverSmsProperty.getSecretKey();

            String message = new StringBuilder()
                    .append(method)
                    .append(space)
                    .append(url)
                    .append(newLine)
                    .append(currentTime)
                    .append(newLine)
                    .append(accessKey)
                    .toString();

            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes(UTF_8));
            return Base64.encodeBase64String(rawHmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException();
        }
    }

    @Getter
    private static class MessagesRequest {

        private String to;
        private String content; /* personal message content */

        public MessagesRequest(String to, String content) {
            this.to = to;
            this.content = content;
        }
    }

    @Getter
    private static class SmsRequest {

        private String type;
        private String from;
        private String content; /* basic message content */
        private List<MessagesRequest> messages;

        public SmsRequest(String type, String from, String content, List<MessagesRequest> messages) {
            this.type = type;
            this.from = from;
            this.content = content;
            this.messages = messages;
        }
    }

    @Getter
    @ToString
    private static class SmsResponse {

        private String requestId;
        private String requestTime;
        private String statusCode;
        private String statusName;
    }
}
