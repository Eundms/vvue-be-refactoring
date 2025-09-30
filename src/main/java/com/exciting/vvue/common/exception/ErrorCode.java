package com.exciting.vvue.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 인증/인가 관련
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "AUTH_001", "error.auth.001"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "error.auth.002"),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "AUTH_003", "error.auth.003"),

    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "error.user.001"),
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER_002", "error.user.002"),
    NICKNAME_NULL(HttpStatus.BAD_REQUEST, "USER_003", "error.user.003"),

    // 결혼 정보 관련
    MARRIED_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "MARRIED_001", "error.married.001"),
    ALREADY_MARRIED(HttpStatus.BAD_REQUEST, "MARRIED_002", "error.married.002"),
    MARRIED_CODE_NOT_GENERATED(HttpStatus.INTERNAL_SERVER_ERROR, "MARRIED_003", "error.married.003"),
    MARRIED_WITH_SAME_ID(HttpStatus.BAD_REQUEST, "MARRIED_004", "error.married.004"),

    // 일정 관련
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE_001", "error.schedule.001"),

    // 메모리 관련
    MEMORY_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMORY_001", "error.memory.001"),
    USER_MEMORY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMORY_002", "error.memory.002"),

    // 장소 관련
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "PLACE_001", "error.place.001"),
    PLACE_REQUIRED(HttpStatus.BAD_REQUEST, "PLACE_002", "error.place.002"),
    FAVORITE_PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "PLACE_003", "error.place.003"),

    // 파일/사진 관련
    PICTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "PICTURE_001", "error.picture.001"),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_001", "error.file.001"),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_002", "error.file.002"),

    // 알림 관련
    NOTIFICATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION_001", "error.notification.001"),
    SUBSCRIBER_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION_002", "error.notification.002"),
    USER_NOT_ADDED_TO_NOTIFY(HttpStatus.BAD_REQUEST, "NOTIFICATION_003", "error.notification.003"),

    // 랜딩 관련
    LANDING_ID_NOT_VALID(HttpStatus.BAD_REQUEST, "LANDING_001", "error.landing.001"),

    // 일반 에러
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_001", "error.common.001"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_002", "error.common.002"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_003", "error.common.003"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "COMMON_004", "error.common.004");

    private final HttpStatus httpStatus;
    private final String code;
    private final String messageKey;
}