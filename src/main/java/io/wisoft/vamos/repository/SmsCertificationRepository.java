package io.wisoft.vamos.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class SmsCertificationRepository {

    private final String PREFIX = "sms:";
    private final int LIMIT_TIME = 3 * 60;

    private final StringRedisTemplate stringRedisTemplate;

    public void createSmsCertification(String phoneNumber, String certificationNumber) {
        stringRedisTemplate.opsForValue()
                .set(PREFIX + phoneNumber, certificationNumber, Duration.ofSeconds(LIMIT_TIME));
    }

    public String getSmsCertification(String phoneNumber) {
        return stringRedisTemplate.opsForValue()
                .get(PREFIX + phoneNumber);
    }

    public void removeSmsCertification(String number) {
        stringRedisTemplate.delete(PREFIX + number);
    }

    public Boolean hasKey(String phoneNumber) {
        return stringRedisTemplate.hasKey(PREFIX + phoneNumber);
    }
}
