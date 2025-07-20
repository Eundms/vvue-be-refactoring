package com.exciting.vvue.schedule;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.schedule.dto.req.ScheduleReqDto;
import com.exciting.vvue.schedule.dto.res.ScheduleDailyResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleListResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleResDto;
import com.exciting.vvue.schedule.model.Schedule;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

  Schedule addSchedule(Married married, ScheduleReqDto scheduleReqDto);

  void addAnniversaryAndBirthday(long marriedId);

  Schedule modifySchedule(Long marriedId, Long scheduleId, ScheduleReqDto scheduleReqDto);

  void deleteSchedule(Long marriedId, Long scheduleId);

  ScheduleListResDto getAllSchedule(Married married, long idCursor, int size);

  boolean hasNext(List<ScheduleResDto> scheduleResDtoList, int size);

  List<String> getScheduledDateOnCalendar(Long marriedId, int year, int month);

  ScheduleResDto getSchedule(Long scheduleId);

  List<ScheduleDailyResDto> getScheduleOnDate(Long marriedId, Long userId, LocalDate date);

  List<Schedule> getAllAfterNDaySchedule(int dayAfter);

  boolean existsById(Long scheduleId);
}
