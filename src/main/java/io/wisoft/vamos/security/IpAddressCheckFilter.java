package io.wisoft.vamos.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class IpAddressCheckFilter extends OncePerRequestFilter {

    private final List<String> keys = List.of(
            "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");

    private final Environment environment;

    private IpAddressCheckFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String port = environment.getProperty("local.server.port");

        log.info("request uri = {}", request.getRequestURI());

        boolean isCheck = false;
        for (String key : keys) {
            String clientIp = request.getHeader(key);
            if (clientIp != null) {
                log.info("[{}] : client ip = {}", port, clientIp);
                isCheck = true;
                break;
            }
        }

        if (!isCheck) log.info("[{}] client ip = {}", port, request.getRemoteAddr());

        filterChain.doFilter(request, response);
    }
}
