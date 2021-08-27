package io.wisoft.vamos.config;

import io.wisoft.vamos.domain.board.BoardInfoCriteria;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
public class BoardInfoCriteriaResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(BoardInfoCriteria.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String id = Optional.ofNullable(webRequest.getParameter("id")).orElseThrow(IllegalArgumentException::new);
        String title = webRequest.getParameter("title");
        String content = webRequest.getParameter("content");
        String price = Optional.ofNullable(webRequest.getParameter("price")).orElseThrow(IllegalArgumentException::new);

        return BoardInfoCriteria.builder()
                .id(Long.parseLong(id))
                .title(title)
                .content(content)
                .price(Integer.parseInt(price));
    }
}
