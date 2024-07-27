package com.exciting.vvue.schedule.model.dto;

import com.exciting.vvue.schedule.model.DateType;
import com.exciting.vvue.schedule.model.RepeatCycle;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleDailyResDto {
    private ScheduleResDto scheduleResDto;
    private Long memoryId;
    private boolean wroteMemory;

    @Builder
    public ScheduleDailyResDto(ScheduleResDto scheduleResDto, Long memoryId, boolean wroteMemory){
        this.scheduleResDto = scheduleResDto;
        this.memoryId = memoryId;
        this.wroteMemory = wroteMemory;
    }

    public static ScheduleDailyResDto from(ScheduleResDto scheduleResDto, Long memoryId, boolean wroteMemory){
        return ScheduleDailyResDto.builder()
                .scheduleResDto(scheduleResDto)
                .memoryId(memoryId)
                .wroteMemory(wroteMemory)
                .build();
    }
}
