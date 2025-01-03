package com.exciting.vvue.common.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	private static final ThreadLocal<String> requestIdThreadLocal = new ThreadLocal<>();
	private static final ThreadLocal<Integer> callDepthThreadLocal = ThreadLocal.withInitial(() -> 0);

	@Pointcut("within(com.exciting.vvue..*) && within(@org.springframework.web.bind.annotation.RestController *)")
	public void controllerMethods() {}

	@Pointcut("within(com.exciting.vvue..*) && within(@org.springframework.stereotype.Service *)")
	public void serviceMethods() {}

	@Pointcut("within(com.exciting.vvue..*) && within(@org.springframework.stereotype.Repository *)")
	public void repositoryMethods() {}

	@Around("controllerMethods() || serviceMethods() || repositoryMethods()")
	public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		// 요청 ID 초기화
		if (requestIdThreadLocal.get() == null) {
			requestIdThreadLocal.set(UUID.randomUUID().toString());
		}

		int callDepth = callDepthThreadLocal.get();
		callDepthThreadLocal.set(callDepth + 1);

		// 접두사 생성
		String entryPrefix = "=".repeat(callDepth) + "=>";
		String exitPrefix = "<=" + "=".repeat(callDepth);

		String requestId = requestIdThreadLocal.get();
		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
		String pointcutName = resolvePointcutName(proceedingJoinPoint);

		logger.info("[{}] [{}] {} [{}] {} | started with arguments: {}",
			requestId,
			currentTime,
			entryPrefix,
			pointcutName,
			proceedingJoinPoint.getSignature().toShortString(),
			proceedingJoinPoint.getArgs());

		long startTime = System.nanoTime();

		try {
			Object result = proceedingJoinPoint.proceed();
			String afterCurrentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
			long duration = (System.nanoTime() - startTime) / 1_000_000; // 밀리초

			logger.info("[{}] [{}] {} [{}] {} | completed in {} ms. Return value: {}",
				requestId,
				afterCurrentTime,
				exitPrefix,
				pointcutName,
				proceedingJoinPoint.getSignature().toShortString(),
				duration,
				result);

			return result;
		} catch (Throwable throwable) {
			String afterCurrentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
			long duration = (System.nanoTime() - startTime) / 1_000_000; // 밀리초

			logger.error("[{}] [{}] {} [{}] {} | threw exception after {} ms: {}",
				requestId,
				afterCurrentTime,
				exitPrefix,
				pointcutName,
				proceedingJoinPoint.getSignature().toShortString(),
				duration,
				throwable.getMessage());
			throw throwable;
		} finally {
			callDepthThreadLocal.set(callDepthThreadLocal.get() - 1);

			if (callDepthThreadLocal.get() == 0) {
				requestIdThreadLocal.remove();
				callDepthThreadLocal.remove();
			}
		}
	}

	private String resolvePointcutName(ProceedingJoinPoint proceedingJoinPoint) {
		Class<?> clazz = proceedingJoinPoint.getSignature().getDeclaringType();
		Class<?> superClass = clazz.getSuperclass();

		if (clazz.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class)
			|| (superClass != null && superClass.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class))) {
			return "controller";
		}

		if (clazz.isAnnotationPresent(org.springframework.stereotype.Service.class)
			|| (superClass != null && superClass.isAnnotationPresent(org.springframework.stereotype.Service.class))) {
			return "service";
		}

		if (clazz.isAnnotationPresent(org.springframework.stereotype.Repository.class)
			|| (superClass != null && superClass.isAnnotationPresent(org.springframework.stereotype.Repository.class))) {
			return "repository";
		}

		return "unknown";
	}
}
