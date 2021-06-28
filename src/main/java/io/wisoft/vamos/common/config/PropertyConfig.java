package io.wisoft.vamos.common.config;

import io.wisoft.vamos.common.property.NaverSmsProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {NaverSmsProperty.class})
public class PropertyConfig {
}
