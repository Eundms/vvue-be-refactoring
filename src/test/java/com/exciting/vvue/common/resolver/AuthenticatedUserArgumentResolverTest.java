package com.exciting.vvue.common.resolver;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.common.annotation.AuthenticatedUser;
import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("AuthenticatedUserArgumentResolver 테스트")
class AuthenticatedUserArgumentResolverTest {

    private AuthenticatedUserArgumentResolver resolver;
    private MethodParameter methodParameter;
    private MockedStatic<AuthContext> authContextMock;

    @BeforeEach
    void setUp() {
        resolver = new AuthenticatedUserArgumentResolver();
        methodParameter = mock(MethodParameter.class);
        authContextMock = Mockito.mockStatic(AuthContext.class);
    }

    @AfterEach
    void tearDown() {
        authContextMock.close();
    }

    @Test
    @DisplayName("@AuthenticatedUser 어노테이션이 있고 Long 타입인 경우 지원한다")
    void supportsParameter() {
        // given
        when(methodParameter.hasParameterAnnotation(AuthenticatedUser.class)).thenReturn(true);
        when(methodParameter.getParameterType()).thenReturn((Class)Long.class);

        // when
        boolean result = resolver.supportsParameter(methodParameter);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("@AuthenticatedUser 어노테이션이 없는 경우 지원하지 않는다")
    void doesNotSupportParameterWithoutAnnotation() {
        // given
        when(methodParameter.hasParameterAnnotation(AuthenticatedUser.class)).thenReturn(false);

        // when
        boolean result = resolver.supportsParameter(methodParameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("사용자 ID가 있는 경우 해당 ID를 반환한다")
    void resolveArgumentWithUserId() throws Exception {
        // given
        Long userId = 123L;
        authContextMock.when(AuthContext::getUserId).thenReturn(userId);

        // when
        Object result = resolver.resolveArgument(methodParameter, null, null, null);

        // then
        assertThat(result).isEqualTo(userId);
    }

    @Test
    @DisplayName("사용자 ID가 없는 경우 VvueException을 던진다")
    void throwsExceptionWhenUserIdIsNull() {
        // given
        authContextMock.when(AuthContext::getUserId).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> resolver.resolveArgument(methodParameter, null, null, null))
                .isInstanceOf(VvueException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.UNAUTHORIZED_USER);
    }
}