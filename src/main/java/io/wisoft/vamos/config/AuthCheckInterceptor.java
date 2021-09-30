package io.wisoft.vamos.config;

import io.wisoft.vamos.dto.api.ErrorCode;
import io.wisoft.vamos.exception.AuthenticationPrincipalException;
import io.wisoft.vamos.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("[AuthCheckInterceptor] authentication = {}", authentication);

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationPrincipalException(ErrorCode.AUTH_INFO);
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        log.info("[AuthCheckInterceptor] principal = {}", principal);
        return true;
    }
}
