package com.exciting.vvue.schedule.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleListResDto {
    List<ScheduleResDto> scheduleResDtoList;
    boolean hasNext;
    Long lastId;

    @Builder
    public ScheduleListResDto(List<ScheduleResDto> scheduleResDtoList, boolean hasNext, Long lastId){
        this.scheduleResDtoList = scheduleResDtoList;
        this.hasNext = hasNext;
        this.lastId = lastId;
    }
}
