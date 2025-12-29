package com.exciting.vvue.common.aop;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.common.logging.StructuredLogger;
import com.exciting.vvue.common.metrics.VvueMetrics;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

  private final StructuredLogger structuredLogger;
  private final VvueMetrics vvueMetrics;

  @Around("execution(* com.exciting.vvue..controller.*.*(..))")
  public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    HttpServletRequest request = getCurrentRequest();
    if (request == null) {
      return joinPoint.proceed();
    }

    String endpoint = request.getRequestURI();
    String method = request.getMethod();
    String userId = getCurrentUserId();
    long startTime = System.currentTimeMillis();

    Timer.Sample timerSample = vvueMetrics.startApiTimer(endpoint, method);

    try {
      Object result = joinPoint.proceed();
      long responseTime = System.currentTimeMillis() - startTime;

      structuredLogger.logApiCall(endpoint, method, userId, responseTime, 200);

      vvueMetrics.recordApiCall(endpoint, method, "success");
      vvueMetrics.recordApiDuration(timerSample, endpoint, method, "success");

      return result;
    } catch (Exception e) {
      long responseTime = System.currentTimeMillis() - startTime;

      structuredLogger.logApiCall(endpoint, method, userId, responseTime, 500);

      vvueMetrics.recordApiCall(endpoint, method, "error");
      vvueMetrics.recordApiDuration(timerSample, endpoint, method, "error");
      vvueMetrics.recordError("CONTROLLER", e.getClass().getSimpleName());

      throw e;
    }
  }

  @Around("execution(* com.exciting.vvue..service.*.*(..))")
  public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    String className = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    String operation = className + "." + methodName;
    String userId = getCurrentUserId();
    long startTime = System.currentTimeMillis();

    try {
      Object result = joinPoint.proceed();
      long executionTime = System.currentTimeMillis() - startTime;

      Map<String, Object> metrics = new HashMap<>();
      metrics.put("className", className);
      metrics.put("methodName", methodName);
      metrics.put("args", joinPoint.getArgs().length);

      structuredLogger.logPerformance(operation, executionTime, userId, metrics);
      return result;
    } catch (Exception e) {
      long executionTime = System.currentTimeMillis() - startTime;
      Map<String, Object> metrics = new HashMap<>();
      metrics.put("className", className);
      metrics.put("methodName", methodName);
      metrics.put("failed", true);

      structuredLogger.logPerformance(operation, executionTime, userId, metrics);

      structuredLogger.logError(className, "SERVICE_ERROR", e.getMessage(), userId, e);
      throw e;
    }
  }

  @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
  public Object logTransactionalMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    String className = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    String userId = getCurrentUserId();
    long startTime = System.currentTimeMillis();

    try {
      Object result = joinPoint.proceed();
      long executionTime = System.currentTimeMillis() - startTime;

      Map<String, Object> details = new HashMap<>();
      details.put("operation", className + "." + methodName);
      details.put("executionTime", executionTime);
      details.put("status", "SUCCESS");

      structuredLogger.logBusinessEvent("TRANSACTION", userId, "COMMIT", details);
      return result;
    } catch (Exception e) {
      long executionTime = System.currentTimeMillis() - startTime;

      Map<String, Object> details = new HashMap<>();
      details.put("operation", className + "." + methodName);
      details.put("executionTime", executionTime);
      details.put("status", "ROLLBACK");
      details.put("error", e.getMessage());

      structuredLogger.logBusinessEvent("TRANSACTION", userId, "ROLLBACK", details);
      throw e;
    }
  }

  private HttpServletRequest getCurrentRequest() {
    ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return attrs != null ? attrs.getRequest() : null;
  }

  private String getCurrentUserId() {
    try {
      Long userId = AuthContext.getUserId();
      return userId != null ? userId.toString() : "anonymous";
    } catch (Exception e) {
      return "anonymous";
    }
  }
}