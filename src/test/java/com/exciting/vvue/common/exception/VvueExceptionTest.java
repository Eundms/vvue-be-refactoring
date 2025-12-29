package com.exciting.vvue.common.exception;

import com.exciting.vvue.common.util.MessageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("VvueException 테스트")
class VvueExceptionTest {

    private MessageUtil messageUtil;
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        messageUtil = mock(MessageUtil.class);
        applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBean(MessageUtil.class)).thenReturn(messageUtil);
    }

    @Test
    @DisplayName("ErrorCode로 예외 생성 시 국제화 메시지가 적용된다")
    void createExceptionWithErrorCode() {
        // given
        String expectedMessage = "사용자를 찾을 수 없습니다.";
        when(messageUtil.getMessage("error.user.001")).thenReturn(expectedMessage);

        try (MockedStatic<VvueException.SpringContextHolder> mockedStatic =
             Mockito.mockStatic(VvueException.SpringContextHolder.class)) {

            mockedStatic.when(() -> VvueException.SpringContextHolder.getBean(MessageUtil.class))
                       .thenReturn(messageUtil);

            // when
            VvueException exception = new VvueException(ErrorCode.USER_NOT_FOUND);

            // then
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
            assertThat(exception.getCustomMessage()).isNull();
            assertThat(exception.getMessage()).isEqualTo(expectedMessage);
        }
    }

    @Test
    @DisplayName("커스텀 메시지로 예외 생성 시 커스텀 메시지가 우선된다")
    void createExceptionWithCustomMessage() {
        // given
        String customMessage = "특별한 사용자 오류 메시지";

        // when
        VvueException exception = new VvueException(ErrorCode.USER_NOT_FOUND, customMessage);

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        assertThat(exception.getCustomMessage()).isEqualTo(customMessage);
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    @DisplayName("MessageUtil이 없는 경우 메시지 키를 반환한다")
    void fallbackToMessageKeyWhenMessageUtilNotAvailable() {
        try (MockedStatic<VvueException.SpringContextHolder> mockedStatic =
             Mockito.mockStatic(VvueException.SpringContextHolder.class)) {

            mockedStatic.when(() -> VvueException.SpringContextHolder.getBean(MessageUtil.class))
                       .thenThrow(new RuntimeException("Bean not found"));

            // when
            VvueException exception = new VvueException(ErrorCode.USER_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("error.user.001");
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        }
    }
}