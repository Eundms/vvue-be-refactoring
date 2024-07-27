package com.exciting.vvue.notification.model;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum NotificationType {
    // 이벤트 기반
    MEMORY("후기"),
    SCHEDULE("일정"),
    // 등록 날짜 기반
    BIRTH("생일"),
    MARRIED("결혼기념일");
    private final String description;
}
