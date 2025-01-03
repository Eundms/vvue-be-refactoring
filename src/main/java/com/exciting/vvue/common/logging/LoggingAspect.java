package com.exciting.vvue.common.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

	// ThreadLocal로 시작 시간을 저장
	private static ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();

	@Pointcut("within(com.exciting.vvue..*) && within(@org.springframework.web.bind.annotation.RestController *)")
	public void controllerMethods() {}

	@Pointcut("within(com.exciting.vvue..*) && within(@org.springframework.stereotype.Service *)")
	public void serviceMethods() {}

	@Pointcut("within(com.exciting.vvue..*) && within(@org.springframework.stereotype.Repository *)")
	public void repositoryMethods() {}

	@Around("controllerMethods() || serviceMethods() || repositoryMethods()")
	public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		// 시작 시간을 ThreadLocal에 저장
		long startTime = System.nanoTime();
		startTimeThreadLocal.set(startTime);

		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
		logger.info("[INFO] [{}] [{}] {} | started with arguments: {}",
			resolvePointcutName(proceedingJoinPoint),
			currentTime,
			proceedingJoinPoint.getSignature().toShortString(),
			proceedingJoinPoint.getArgs());

		try {
			// 실제 메서드 호출
			Object result = proceedingJoinPoint.proceed(); // ProceedingJoinPoint에서 proceed() 호출

			// 메서드 실행 후 종료 시간 계산
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1_000_000; // 밀리초로 변환
			logger.info("[INFO] [{}] [{}] {} | duration: {} ms. Return value: {}",
				resolvePointcutName(proceedingJoinPoint),
				currentTime,
				proceedingJoinPoint.getSignature().toShortString(),
				duration,
				result);

			return result;
		} catch (Throwable throwable) {
			// 예외 발생 시 처리
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1_000_000; // 밀리초로 변환
			logger.error("[ERROR] [{}] [{}] {} | threw exception after {} ms: {}",
				resolvePointcutName(proceedingJoinPoint),
				currentTime,
				proceedingJoinPoint.getSignature().toShortString(),
				duration,
				throwable.getMessage());
			throw throwable;
		} finally {
			// ThreadLocal 값 정리
			startTimeThreadLocal.remove();
		}
	}

	private String resolvePointcutName(ProceedingJoinPoint proceedingJoinPoint) {
		Class<?> clazz = proceedingJoinPoint.getSignature().getDeclaringType();
		Class<?> superClass = clazz.getSuperclass();

		if (clazz.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class)
			|| superClass != null && superClass.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class)) {
			return "controller";
		}

		if (clazz.isAnnotationPresent(org.springframework.stereotype.Service.class)
			|| superClass != null && superClass.isAnnotationPresent(org.springframework.stereotype.Service.class)) {
			return "service";
		}

		if (clazz.isAnnotationPresent(org.springframework.stereotype.Repository.class)
			|| superClass != null && superClass.isAnnotationPresent(org.springframework.stereotype.Repository.class)) {
			return "repository";
		}

		return "unknown";
	}
}
