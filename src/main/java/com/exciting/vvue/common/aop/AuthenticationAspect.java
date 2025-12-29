package com.exciting.vvue.common.aop;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.common.annotation.RequireMarried;
import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
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
            throw new VvueException(ErrorCode.UNAUTHORIZED_USER);
        }

        Married married = marriedService.getMarriedByUserIdWithDetails(userId);
        if (married == null) {
            throw new VvueException(ErrorCode.MARRIED_INFO_NOT_FOUND);
        }

        log.debug("Validated married user: userId={}, marriedId={}", userId, married.getId());
    }
}