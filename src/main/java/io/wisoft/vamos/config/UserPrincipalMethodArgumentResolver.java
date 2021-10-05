package io.wisoft.vamos.config;

import io.wisoft.vamos.dto.api.ErrorCode;
import io.wisoft.vamos.exception.AuthenticationPrincipalException;
import io.wisoft.vamos.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class UserPrincipalMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("UserPrincipalMethodArgumentResolver.authentication = {}", authentication);

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationPrincipalException(ErrorCode.AUTHENTICATION_INFO);
        }

        return authentication.getPrincipal();
    }
}
