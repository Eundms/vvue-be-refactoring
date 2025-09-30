package com.exciting.vvue.common.aop;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.common.annotation.RequireMarried;
import com.exciting.vvue.common.exception.user.UserUnAuthorizedException;
import com.exciting.vvue.common.exception.married.MarriedInfoNotFoundException;
import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.married.model.Married;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAspect {

    private final MarriedService marriedService;

    @Before("@annotation(requireMarried) || @within(requireMarried)")
    public void validateMarriedUser(JoinPoint joinPoint, RequireMarried requireMarried) {
        Long userId = AuthContext.getUserId();
        if (userId == null) {
            throw new UserUnAuthorizedException("인증되지 않은 사용자입니다.");
        }

        Married married = marriedService.getMarriedByUserIdWithDetails(userId);
        if (married == null) {
            throw new MarriedInfoNotFoundException("결혼 정보가 없습니다.");
        }

        log.debug("Validated married user: userId={}, marriedId={}", userId, married.getId());
    }
}