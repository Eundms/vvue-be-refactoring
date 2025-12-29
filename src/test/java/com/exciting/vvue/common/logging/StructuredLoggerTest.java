package com.exciting.vvue.common.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StructuredLogger 테스트")
class StructuredLoggerTest {

    private StructuredLogger structuredLogger;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        structuredLogger = new StructuredLogger(objectMapper);

        // 로그 출력을 캡처하기 위한 설정
        Logger logger = (Logger) LoggerFactory.getLogger(StructuredLogger.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        logger.setLevel(Level.INFO);
    }

    @Test
    @DisplayName("인증 성공 시 INFO 레벨로 로그가 기록된다")
    void logAuthenticationAttemptSuccess() {
        // when
        structuredLogger.logAuthenticationAttempt("user123", "OAUTH", true);

        // then
        List<ILoggingEvent> logsList = listAppender.list;
        assertThat(logsList).hasSize(1);
        assertThat(logsList.get(0).getLevel()).isEqualTo(Level.INFO);
        assertThat(logsList.get(0).getMessage()).contains("Authentication successful");
        assertThat(logsList.get(0).getFormattedMessage()).contains("user123");
        assertThat(logsList.get(0).getFormattedMessage()).contains("OAUTH");
    }

    @Test
    @DisplayName("인증 실패 시 WARN 레벨로 로그가 기록된다")
    void logAuthenticationAttemptFailure() {
        // when
        structuredLogger.logAuthenticationAttempt("user123", "OAUTH", false);

        // then
        List<ILoggingEvent> logsList = listAppender.list;
        assertThat(logsList).hasSize(1);
        assertThat(logsList.get(0).getLevel()).isEqualTo(Level.WARN);
        assertThat(logsList.get(0).getMessage()).contains("Authentication failed");
    }

    @Test
    @DisplayName("API 호출 성공 시 INFO 레벨로 로그가 기록된다")
    void logApiCallSuccess() {
        // when
        structuredLogger.logApiCall("/api/users", "GET", "user123", 150L, 200);

        // then
        List<ILoggingEvent> logsList = listAppender.list;
        assertThat(logsList).hasSize(1);
        assertThat(logsList.get(0).getLevel()).isEqualTo(Level.INFO);
        assertThat(logsList.get(0).getMessage()).contains("API call");
        assertThat(logsList.get(0).getFormattedMessage()).contains("/api/users");
        assertThat(logsList.get(0).getFormattedMessage()).contains("GET");
        assertThat(logsList.get(0).getFormattedMessage()).contains("150");
    }

    @Test
    @DisplayName("API 호출 실패 시 WARN 레벨로 로그가 기록된다")
    void logApiCallFailure() {
        // when
        structuredLogger.logApiCall("/api/users", "GET", "user123", 150L, 500);

        // then
        List<ILoggingEvent> logsList = listAppender.list;
        assertThat(logsList).hasSize(1);
        assertThat(logsList.get(0).getLevel()).isEqualTo(Level.WARN);
        assertThat(logsList.get(0).getMessage()).contains("API call failed");
    }

    @Test
    @DisplayName("비즈니스 이벤트가 올바른 형식으로 로그에 기록된다")
    void logBusinessEvent() {
        // given
        Map<String, Object> details = new HashMap<>();
        details.put("scheduleId", 123L);
        details.put("action", "CREATE");

        // when
        structuredLogger.logBusinessEvent("SCHEDULE", "user123", "CREATE_SCHEDULE", details);

        // then
        List<ILoggingEvent> logsList = listAppender.list;
        assertThat(logsList).hasSize(1);
        assertThat(logsList.get(0).getLevel()).isEqualTo(Level.INFO);
        assertThat(logsList.get(0).getMessage()).contains("Business event");
        assertThat(logsList.get(0).getFormattedMessage()).contains("SCHEDULE");
        assertThat(logsList.get(0).getFormattedMessage()).contains("CREATE_SCHEDULE");
    }

    @Test
    @DisplayName("성능 로그에서 느린 작업이 WARN 레벨로 기록된다")
    void logSlowPerformance() {
        // when
        structuredLogger.logPerformance("SlowService.slowMethod", 1500L, "user123", new HashMap<>());

        // then
        List<ILoggingEvent> logsList = listAppender.list;
        assertThat(logsList).hasSize(1);
        assertThat(logsList.get(0).getLevel()).isEqualTo(Level.WARN);
        assertThat(logsList.get(0).getMessage()).contains("Slow operation detected");
    }

    @Test
    @DisplayName("성능 로그에서 빠른 작업이 INFO 레벨로 기록된다")
    void logFastPerformance() {
        // when
        structuredLogger.logPerformance("FastService.fastMethod", 500L, "user123", new HashMap<>());

        // then
        List<ILoggingEvent> logsList = listAppender.list;
        assertThat(logsList).hasSize(1);
        assertThat(logsList.get(0).getLevel()).isEqualTo(Level.INFO);
        assertThat(logsList.get(0).getMessage()).contains("Performance metric");
    }
}