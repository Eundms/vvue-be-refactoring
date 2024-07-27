package com.exciting.vvue.memory.model.dto.res;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleResDto {

    // 일정 관련 데이터
    private Long id; // 일정 ID
    private String name;
    private String date;
    @Builder
    public ScheduleResDto( Long id, String name,
        LocalDate date) {
        this.id = id;
        this.name = name;
        this.date = date.toString();
    }
}
