package com.exciting.vvue.schedule.model.dto;

import com.exciting.vvue.schedule.model.DateType;
import com.exciting.vvue.schedule.model.RepeatCycle;
import com.exciting.vvue.schedule.model.Schedule;
import java.time.format.DateTimeFormatter;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@Getter
@NoArgsConstructor
public class ScheduleResDto {
    private Long id;
    private Long marriedId;
    private String scheduleDate;
    private String curDate;
    private String scheduleName;
    private RepeatCycle repeatCycle;
    private DateType dateType;

    @Builder
    public ScheduleResDto(Long id, Long marriedId, String scheduleDate, String scheduleName, RepeatCycle repeatCycle, DateType dateType){
        this.id = id;
        this.marriedId = marriedId;
        this.curDate = scheduleDate;
        this.scheduleDate = scheduleDate;
        this.scheduleName = scheduleName;
        this.repeatCycle = repeatCycle;
        this.dateType = dateType;
    }

    // curDate에 반복 주기 중 가장 최신 날짜로 받기
    // 예시 : 오늘이 2023-09-19, scheduleDate 2021-09-24 면, curDate = 2023-09-24 로 넣어주기
    public ScheduleResDto setCommingDate(){
        LocalDate origin = LocalDate.parse(this.scheduleDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate scheduleLocalDate = LocalDate.parse(this.scheduleDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        switch (this.getRepeatCycle()){
            // 매달 반복
            case MONTHLY -> {
                int i = 1;
                LocalDate curLocalDate;
                do{
                    curLocalDate = scheduleLocalDate.plusMonths(Math.max(Period.between(scheduleLocalDate, LocalDate.now().minusDays(1)).toTotalMonths() - (scheduleLocalDate.isBefore(LocalDate.now().minusDays(1))?0:1) + i++,0));
                } while(curLocalDate.getDayOfMonth() != scheduleLocalDate.getDayOfMonth());
                curDate = curLocalDate.toString();

            }
            // 매년 반복
            case YEARLY -> {
                int i = 1;
                LocalDate curLocalDate;
                do{
                    curLocalDate = scheduleLocalDate.plusYears(Math.max(Period.between(scheduleLocalDate,LocalDate.now().minusDays(1)).getYears() - (scheduleLocalDate.isBefore(LocalDate.now().minusDays(1))?0:1) + i++,0));
                } while(curLocalDate.getDayOfMonth() != scheduleLocalDate.getDayOfMonth());
                curDate = curLocalDate.toString();
            }
        }

        return this;
    }

    public ScheduleResDto setCurDate(LocalDate date){
        this.curDate = date.toString();
        return this;
    }

    public static ScheduleResDto from(Schedule schedule){
        return ScheduleResDto.builder()
                .id(schedule.getId())
                .marriedId(schedule.getMarried().getId())
                .scheduleDate(schedule.getScheduleDate().toString())
                .scheduleName(schedule.getScheduleName())
                .repeatCycle(schedule.getRepeatCycle())
                .dateType(schedule.getDateType())
                .build();
    }
}
