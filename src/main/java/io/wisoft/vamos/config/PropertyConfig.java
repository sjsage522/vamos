package io.wisoft.vamos.config;

import io.wisoft.vamos.property.EmailProperty;
import io.wisoft.vamos.property.NaverSmsProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {NaverSmsProperty.class, EmailProperty.class})
public class PropertyConfig {
}
