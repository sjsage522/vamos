package io.wisoft.vamos.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ProfileController {

    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> productionProfiles = Arrays.asList("prod1", "prod2");
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);

        log.info("profiles : {}", profiles);
        return profiles.stream()
                .filter(productionProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
