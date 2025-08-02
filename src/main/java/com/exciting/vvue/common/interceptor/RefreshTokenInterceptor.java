package com.exciting.vvue.common.interceptor;

import com.exciting.vvue.auth.jwt.exception.InvalidTokenException;
import com.exciting.vvue.common.annotation.NoAuth;
import java.lang.reflect.Method;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {

    if (isPreflightRequest(request) || isNoAuthAnnotated(handler)) {
      return true;
    }

    String token = resolveToken(request);

    if (token == null || token.isBlank()) {
      log.info("토큰이 제공되지 않음.");
      throw new InvalidTokenException("토큰이 제공되지 않았습니다.");
    }

    return true;
  }

  private boolean isPreflightRequest(HttpServletRequest request) {
    return HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod());
  }

  private boolean isNoAuthAnnotated(Object handler) {
    if (handler instanceof HandlerMethod handlerMethod) {
      Method method = handlerMethod.getMethod();
      return method.isAnnotationPresent(NoAuth.class);
    }
    return false;
  }

  private String resolveToken(HttpServletRequest request) {
    String refresh = request.getHeader("refresh-token");
    if (refresh != null && refresh.startsWith("Bearer ")) {
      return refresh.substring(7);
    }
    return null;
  }

}