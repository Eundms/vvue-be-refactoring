package com.exciting.vvue.schedule.service;

import com.exciting.vvue.schedule.model.Schedule;
import com.exciting.vvue.schedule.model.dto.ScheduleDailyResDto;
import com.exciting.vvue.schedule.model.dto.ScheduleListResDto;
import com.exciting.vvue.schedule.model.dto.ScheduleResDto;
import com.exciting.vvue.schedule.model.dto.ScheduleReqDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    Schedule addSchedule(Long marriedId, ScheduleReqDto scheduleReqDto);
    void addAnniversaryAndBirthday(long marriedId);
    Schedule modifySchedule(Long marriedId, Long scheduleId, ScheduleReqDto scheduleReqDto);
    void deleteSchedule(Long marriedId, Long scheduleId);
    ScheduleListResDto getAllSchedule(Long marriedId, long idCursor, int size);
    boolean hasNext(List<ScheduleResDto> scheduleResDtoList, int size);
    List<String> getScheduledDateOnCalendar(Long marriedId, int year, int month);
    ScheduleResDto getSchedule(Long scheduleId);
    List<ScheduleDailyResDto> getScheduleOnDate(Long marriedId, Long userId, LocalDate date);
    List<Schedule> getAllAfterNDaySchedule(int dayAfter);
}
