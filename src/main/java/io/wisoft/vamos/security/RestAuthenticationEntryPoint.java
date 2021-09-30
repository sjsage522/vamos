package io.wisoft.vamos.security;

import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        logger.error("Responding with unauthorized error. Message - {}", e.getMessage());

        httpServletResponse.setStatus(SC_UNAUTHORIZED);
        JSONObject json = new JSONObject();
        json.put("data", null);

        JSONObject errorJson = new JSONObject();
        errorJson.put("message", e.getLocalizedMessage());
        errorJson.put("status", SC_UNAUTHORIZED);
        json.put("error", errorJson);

        httpServletResponse.getOutputStream()
                .println(json.toJSONString());
    }
}
