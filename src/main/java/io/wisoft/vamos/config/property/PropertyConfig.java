package io.wisoft.vamos.config.property;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        NaverSmsProperty.class,
        EmailProperty.class,
        JwtProperty.class,
        AmazonS3Property.class
})
public class PropertyConfig {
}
