package com.exciting.vvue.common.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.auth.jwt.exception.InvalidTokenException;
import com.exciting.vvue.auth.jwt.util.JwtUtil;
import com.exciting.vvue.common.annotation.NoAuth;
import com.exciting.vvue.user.UserService;
import com.exciting.vvue.user.exception.UserNotFoundException;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccessTokenInterceptor implements HandlerInterceptor {
	private final JwtUtil jwtUtil;
	private final UserService userService;

	public AccessTokenInterceptor(JwtUtil jwtUtil, UserService userService) {
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		if (isPreflightRequest(request) || isNoAuthAnnotated(handler)) {
			return true;
		}

		String token = resolveToken(request);
		try {
			validateToken(token);
		} catch (Exception e) {
			return false;
		}
		setAuthenticatedUser(request, token);

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) throws Exception {
		// 요청 처리 후 ThreadLocal 정리
		AuthContext.clear();
	}

	private boolean isPreflightRequest(HttpServletRequest request) {
		return HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod());
	}

	private boolean isNoAuthAnnotated(Object handler) {
		if (handler instanceof HandlerMethod handlerMethod) {
			Method method = handlerMethod.getMethod();
			log.info("No Auth Added?");
			log.info(method.getName());
			return method.isAnnotationPresent(NoAuth.class);
		}
		return false;
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	private void validateToken(String token) {
		if (token == null || token.isBlank()) {
			log.info("validateToken : No Token provided");
			throw new InvalidTokenException("Token Not Provided");
		}

		try {
			if (!jwtUtil.validateToken(token)) {
				log.info("validateToken : Invalid Token : {}", token);
				throw new InvalidTokenException("Token is Invalid");
			}
			log.info("valid Token : {}", token);
		} catch (JwtException e) {
			log.error("Exception Occur Validate Token : {}", token, e);
			throw new InvalidTokenException("Exception Occur Validate Token");
		}
	}

	private void setAuthenticatedUser(HttpServletRequest request, String token) {
		String userId = jwtUtil.getClaimBy(token, "id");

		try {
			if (userService.existsById(Long.valueOf(userId))) {
				AuthContext.setUserId(Long.valueOf(userId));
			}
			log.info("Valid User Set : userId={}", userId);
		} catch (UserNotFoundException e) {
			log.error("Not Valid User: userId={}", userId, e);
			throw new InvalidTokenException("Not Valid User");
		}
	}
}