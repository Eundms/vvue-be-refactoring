package com.exciting.vvue.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;

@Slf4j
@Component
public class Jwt2Interceptor implements HandlerInterceptor {

    private static final String HEADER_AUTH = "Authorization";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler)
            throws Exception {
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        log.debug("request : " + request.getHeader(HEADER_AUTH));

        final String token = request.getHeader(HEADER_AUTH);

        log.debug("Interceptor 여기서는 토큰이 필요하지 않습니다! = " + token);
        return true;

    }
}