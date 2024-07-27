package com.exciting.vvue.common.config;


import com.exciting.vvue.common.exception.VvueApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
//@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(VvueApiException.class)
    protected ResponseEntity<Object> handleApiException(VvueApiException exception,
        WebRequest request) {
        log.error("VvueApiException({}) 발생 :  {}",exception.getClass().getName(), exception.getMessage());
        return handleExceptionInternal(exception, null, new HttpHeaders(),
            exception.getStatus(), request);
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleApiException(Exception exception, WebRequest request) {
        return handleExceptionInternal(exception, null , new HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return ResponseEntity.status(status).headers(headers)
            .body("{\"message\":\"" + ex.getMessage() + "\"}");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error("Exception 처리 : 입력값 검증 예외 처리"+ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"" + message + "\"}");
    }
}
