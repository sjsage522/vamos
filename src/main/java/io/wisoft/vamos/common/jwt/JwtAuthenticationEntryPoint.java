package io.wisoft.vamos.common.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Consumer;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final String exception = (String) request.getAttribute("exception");
        if (exception == null) {
            ErrorCode unknownAccess = ErrorCode.UNKNOWN_ACCESS;
            response.setStatus(unknownAccess.getStatus());
            setResponse(response, unknownAccess);
            return;
        }

        logger.info("exception : {}", exception);

        filterError(ErrorCode.values(),
                errorCode -> {
                    if (exception.equals(errorCode.getCode())) {
                        try {
                            setResponse(response, errorCode);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
        ;
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println("{ \"message\" : \"" + errorCode.getMessage()
                + "\", \"code\" : \"" + errorCode.getCode()
                + "\", \"status\" : " + errorCode.getStatus()
                + " }");
    }

    private void filterError(ErrorCode[] errorCodes, Consumer<ErrorCode> filter) {

        for (ErrorCode errorCode : errorCodes) filter.accept(errorCode);
    }
}
