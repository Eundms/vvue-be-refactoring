package com.exciting.vvue.common.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StructuredLogger {

  private final ObjectMapper objectMapper;

  public void logAuthenticationAttempt(String userId, String method, boolean success) {
    Map<String, Object> logData = createBaseLogData("AUTHENTICATION");
    logData.put("userId", userId);
    logData.put("method", method);
    logData.put("success", success);
    logData.put("action", "LOGIN_ATTEMPT");

    if (success) {
      log.info("Authentication successful: {}", toJson(logData));
    } else {
      log.warn("Authentication failed: {}", toJson(logData));
    }
  }

  public void logApiCall(String endpoint, String method, String userId, long responseTime,
      int statusCode) {
    Map<String, Object> logData = createBaseLogData("API_CALL");
    logData.put("endpoint", endpoint);
    logData.put("httpMethod", method);
    logData.put("userId", userId);
    logData.put("responseTimeMs", responseTime);
    logData.put("statusCode", statusCode);

    if (statusCode >= 400) {
      log.warn("API call failed: {}", toJson(logData));
    } else {
      log.info("API call: {}", toJson(logData));
    }
  }

  public void logBusinessEvent(String eventType, String userId, String action,
      Map<String, Object> details) {
    Map<String, Object> logData = createBaseLogData("BUSINESS_EVENT");
    logData.put("eventType", eventType);
    logData.put("userId", userId);
    logData.put("action", action);
    logData.put("details", details);

    log.info("Business event: {}", toJson(logData));
  }

  public void logError(String component, String errorCode, String message, String userId,
      Exception exception) {
    Map<String, Object> logData = createBaseLogData("ERROR");
    logData.put("component", component);
    logData.put("errorCode", errorCode);
    logData.put("message", message);
    logData.put("userId", userId);
    logData.put("exceptionType", exception != null ? exception.getClass().getSimpleName() : null);
    logData.put("stackTrace", exception != null ? getStackTrace(exception) : null);

    log.error("Application error: {}", toJson(logData));
  }

  public void logPerformance(String operation, long executionTime, String userId,
      Map<String, Object> metrics) {
    Map<String, Object> logData = createBaseLogData("PERFORMANCE");
    logData.put("operation", operation);
    logData.put("executionTimeMs", executionTime);
    logData.put("userId", userId);
    logData.put("metrics", metrics);

    if (executionTime > 1000) { // 1초 이상
      log.warn("Slow operation detected: {}", toJson(logData));
    } else {
      log.info("Performance metric: {}", toJson(logData));
    }
  }

  public void logDatabaseOperation(String operation, String table, long executionTime,
      int recordCount) {
    Map<String, Object> logData = createBaseLogData("DATABASE");
    logData.put("operation", operation);
    logData.put("table", table);
    logData.put("executionTimeMs", executionTime);
    logData.put("recordCount", recordCount);

    if (executionTime > 500) {
      log.warn("Slow database operation: {}", toJson(logData));
    } else {
      log.debug("Database operation: {}", toJson(logData));
    }
  }

  private Map<String, Object> createBaseLogData(String logType) {
    Map<String, Object> logData = new HashMap<>();
    logData.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    logData.put("logType", logType);
    logData.put("traceId", getTraceId());
    logData.put("service", "vvue-api");
    logData.put("version", "1.0.0");
    return logData;
  }

  private String getTraceId() {
    String traceId = MDC.get("traceId");
    if (traceId == null) {
      traceId = UUID.randomUUID().toString().substring(0, 8);
      MDC.put("traceId", traceId);
    }
    return traceId;
  }

  private String toJson(Map<String, Object> data) {
    try {
      return objectMapper.writeValueAsString(data);
    } catch (Exception e) {
      return data.toString();
    }
  }

  private String getStackTrace(Exception exception) {
    return java.util.Arrays.stream(exception.getStackTrace())
        .limit(5)
        .map(StackTraceElement::toString)
        .reduce((a, b) -> a + "\n" + b)
        .orElse("");
  }
}