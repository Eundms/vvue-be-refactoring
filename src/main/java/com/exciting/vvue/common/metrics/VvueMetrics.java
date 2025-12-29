package com.exciting.vvue.common.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VvueMetrics {

  private final MeterRegistry meterRegistry;

  public void recordApiCall(String endpoint, String method, String status) {
    Counter.builder("vvue_api_calls_total")
        .description("Total number of API calls")
        .tag("endpoint", endpoint)
        .tag("method", method)
        .tag("status", status)
        .register(meterRegistry)
        .increment();
  }

  public Timer.Sample startApiTimer(String endpoint, String method) {
    return Timer.start(meterRegistry);
  }

  public void recordApiDuration(Timer.Sample sample, String endpoint, String method,
      String status) {
    sample.stop(Timer.builder("vvue_api_duration_seconds")
        .description("API call duration")
        .tag("endpoint", endpoint)
        .tag("method", method)
        .tag("status", status)
        .register(meterRegistry));
  }

  public void recordAuthenticationAttempt(String method, boolean success) {
    Counter.builder("vvue_auth_attempts_total")
        .description("Total authentication attempts")
        .tag("method", method)
        .tag("result", success ? "success" : "failure")
        .register(meterRegistry)
        .increment();
  }

  public void recordBusinessEvent(String eventType, String action) {
    Counter.builder("vvue_business_events_total")
        .description("Total business events")
        .tag("event_type", eventType)
        .tag("action", action)
        .register(meterRegistry)
        .increment();
  }

  public void recordError(String component, String errorCode) {
    Counter.builder("vvue_errors_total")
        .description("Total application errors")
        .tag("component", component)
        .tag("error_code", errorCode)
        .register(meterRegistry)
        .increment();
  }

  public void recordDatabaseOperation(String operation, String table, long executionTime) {
    Timer.builder("vvue_database_duration_seconds")
        .description("Database operation duration")
        .tag("operation", operation)
        .tag("table", table)
        .register(meterRegistry)
        .record(executionTime, java.util.concurrent.TimeUnit.MILLISECONDS);

    Counter.builder("vvue_database_operations_total")
        .description("Total database operations")
        .tag("operation", operation)
        .tag("table", table)
        .register(meterRegistry)
        .increment();
  }

  public void recordUserActivity(String userId, String activity) {
    Counter.builder("vvue_user_activities_total")
        .description("Total user activities")
        .tag("activity", activity)
        .register(meterRegistry)
        .increment();
  }

  public void recordMemoryUsage() {
    Runtime runtime = Runtime.getRuntime();
    long totalMemory = runtime.totalMemory();
    long freeMemory = runtime.freeMemory();
    long usedMemory = totalMemory - freeMemory;

    meterRegistry.gauge("vvue_memory_used_bytes", usedMemory);
    meterRegistry.gauge("vvue_memory_total_bytes", totalMemory);
    meterRegistry.gauge("vvue_memory_free_bytes", freeMemory);
  }

  public void incrementCustomCounter(String name, String... tags) {
    Counter.Builder builder = Counter.builder(name);
    for (int i = 0; i < tags.length; i += 2) {
      if (i + 1 < tags.length) {
        builder.tag(tags[i], tags[i + 1]);
      }
    }
    builder.register(meterRegistry).increment();
  }

  public Timer.Sample startCustomTimer() {
    return Timer.start(meterRegistry);
  }

  public void recordCustomDuration(Timer.Sample sample, String name, String... tags) {
    Timer.Builder builder = Timer.builder(name);
    for (int i = 0; i < tags.length; i += 2) {
      if (i + 1 < tags.length) {
        builder.tag(tags[i], tags[i + 1]);
      }
    }
    sample.stop(builder.register(meterRegistry));
  }
}