package io.wisoft.vamos.config;

import io.wisoft.vamos.config.property.EmailProperty;
import io.wisoft.vamos.config.property.JwtProperty;
import io.wisoft.vamos.config.property.NaverSmsProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {NaverSmsProperty.class, EmailProperty.class, JwtProperty.class})
public class PropertyConfig {
}
