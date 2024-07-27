package com.exciting.vvue.schedule.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RepeatCycle {
    NONREPEAT("반복없음"),
    MONTHLY("매달 반복"),
    YEARLY("매년 반복");
    private final String desc;
}
