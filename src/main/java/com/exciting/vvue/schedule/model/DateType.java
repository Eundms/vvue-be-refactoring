package com.exciting.vvue.schedule.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DateType {
    NORMAL("일반"),
    MALEBIRTHDAY("남편생일"),
    FEMALEBIRTHDAY("아내생일"),
    WEDDINGANNIVERSARY("결혼기념일");

    private final String description;
}
