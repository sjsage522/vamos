package io.wisoft.vamos.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("ncs")
public class NaverSmsProperty {

    private final String accessKey;
    private final String serviceId;
    private final String secretKey;
    private final String from;
}
