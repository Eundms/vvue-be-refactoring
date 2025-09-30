package com.exciting.vvue.common.exception;

import com.exciting.vvue.common.util.MessageUtil;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Getter
public class VvueException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String customMessage;

  public VvueException(ErrorCode errorCode) {
    super(getMessage(errorCode));
    this.errorCode = errorCode;
    this.customMessage = null;
  }

  public VvueException(ErrorCode errorCode, String customMessage) {
    super(customMessage);
    this.errorCode = errorCode;
    this.customMessage = customMessage;
  }

  public VvueException(ErrorCode errorCode, Throwable cause) {
    super(getMessage(errorCode), cause);
    this.errorCode = errorCode;
    this.customMessage = null;
  }

  public VvueException(ErrorCode errorCode, String customMessage, Throwable cause) {
    super(customMessage, cause);
    this.errorCode = errorCode;
    this.customMessage = customMessage;
  }

  private static String getMessage(ErrorCode errorCode) {
    try {
      MessageUtil messageUtil = SpringContextHolder.getBean(MessageUtil.class);
      return messageUtil.getMessage(errorCode.getMessageKey());
    } catch (Exception e) {
      // MessageUtil을 가져올 수 없는 경우 기본 메시지 키 반환
      return errorCode.getMessageKey();
    }
  }

  @Component
  public static class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
      SpringContextHolder.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
      return applicationContext.getBean(clazz);
    }
  }
}