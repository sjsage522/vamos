package io.wisoft.vamos.security;

import io.wisoft.vamos.dto.api.ErrorCode;
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
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");

        JSONObject json = new JSONObject();
        json.put("data", null);

        JSONObject errorJson = new JSONObject();
        errorJson.put("message", ErrorCode.AUTHENTICATION_INFO.getMessage());
        errorJson.put("status", SC_UNAUTHORIZED);
        json.put("error", errorJson);

        httpServletResponse.getWriter()
                .println(json);
    }
}
