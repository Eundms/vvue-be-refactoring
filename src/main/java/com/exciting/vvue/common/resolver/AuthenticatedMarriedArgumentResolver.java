package com.exciting.vvue.common.resolver;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.common.annotation.AuthenticatedMarried;
import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.married.model.Married;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticatedMarriedArgumentResolver implements HandlerMethodArgumentResolver {

    private final MarriedService marriedService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedMarried.class)
                && Married.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Long userId = AuthContext.getUserId();
        if (userId == null) {
            throw new VvueException(ErrorCode.UNAUTHORIZED_USER);
        }

        Married married = marriedService.getMarriedByUserIdWithDetails(userId);
        if (married == null) {
            throw new VvueException(ErrorCode.MARRIED_INFO_NOT_FOUND);
        }

        return married;
    }
}