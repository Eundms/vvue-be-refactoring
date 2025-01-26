package com.exciting.vvue.common.logging;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class LoggingAspect {
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	private final MeterRegistry meterRegistry;

	public LoggingAspect(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}

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

		String requestId = requestIdThreadLocal.get();
		String pointcutName = resolvePointcutName(proceedingJoinPoint);

		String methodName = proceedingJoinPoint.getSignature().toShortString();
		Timer timer = meterRegistry.timer("method_execution_time", "method", methodName, "type", pointcutName);
		Counter errorCounter = meterRegistry.counter("method_execution_error", "method", methodName, "type", pointcutName);

		long startTime = System.nanoTime();

		logger.info("[{}] {} started with arguments: {}", requestId, methodName, proceedingJoinPoint.getArgs());
		try {
			// 실행 시간 기록
			return timer.recordCallable(() -> {
				try {
					return proceedingJoinPoint.proceed();
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			});
		} catch (Throwable throwable) {
			// 에러 카운트 증가
			errorCounter.increment();
			logger.error("[{}] {} threw exception: {}", requestId, methodName, throwable.getMessage());
			throw throwable;
		} finally {
			callDepthThreadLocal.set(callDepthThreadLocal.get() - 1);

			if (callDepthThreadLocal.get() == 0) {
				requestIdThreadLocal.remove();
				callDepthThreadLocal.remove();
			}

			long duration = (System.nanoTime() - startTime) / 1_000_000; // 밀리초
			logger.info("[{}] {} completed in {} ms", requestId, methodName, duration);
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
