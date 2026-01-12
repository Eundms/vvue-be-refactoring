package com.exciting.vvue.schedule.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "특정 날짜 일정 목록 응답")
public class ScheduleDailyListResDto {

  @Schema(description = "조회 날짜")
  private LocalDate date;

  @Schema(description = "일정 개수")
  private int count;

  @Schema(description = "일정 목록")
  private List<ScheduleDailyResDto> schedules;

  public static ScheduleDailyListResDto of(LocalDate date, List<ScheduleDailyResDto> schedules) {
    return new ScheduleDailyListResDto(date, schedules.size(), schedules);
  }
}
