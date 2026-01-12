package com.exciting.vvue.schedule.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleIdResDto {

  private Long scheduleId;

  public static ScheduleIdResDto of(Long scheduleId) {
    return new ScheduleIdResDto(scheduleId);
  }
}
