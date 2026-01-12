package com.exciting.vvue.schedule.dto.res;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalendarResDto {

  private int year;
  private int month;
  private List<String> scheduledDates;

  public static CalendarResDto of(int year, int month, List<String> scheduledDates) {
    return new CalendarResDto(year, month, scheduledDates);
  }
}
