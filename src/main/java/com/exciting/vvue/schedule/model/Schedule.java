package com.exciting.vvue.schedule.model;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.schedule.model.dto.ScheduleReqDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Married married;
    private LocalDate scheduleDate;
    @Column(length = 60)
    private String scheduleName;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(12)")
    private RepeatCycle repeatCycle;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(20)")
    private DateType dateType;

    @Builder
    public Schedule(Long id, Married married, LocalDate scheduleDate, String scheduleName, RepeatCycle repeatCycle, DateType dateType){
        this.id = id;
        this.married = married;
        this.scheduleDate = scheduleDate;
        this.scheduleName = scheduleName;
        this.repeatCycle = repeatCycle;
        this.dateType = dateType;
    }

    public void updateSchedule(ScheduleReqDto scheduleReqDto){
        this.scheduleDate = scheduleReqDto.getScheduleDate();
        this.scheduleName = scheduleReqDto.getScheduleName();
        this.repeatCycle = scheduleReqDto.getRepeatCycle();
    }

    public static Schedule marryAll(Married married, LocalDate date, DateType dateType){
        return Schedule.builder()
                .married(married)
                .scheduleName(dateType.getDescription())
                .scheduleDate(date)
                .repeatCycle(RepeatCycle.YEARLY)
                .dateType(dateType)
                .build();
    }

    public static Schedule from(ScheduleReqDto scheduleReqDto, Married married){
        return Schedule.builder()
                .married(married)
                .scheduleDate(scheduleReqDto.getScheduleDate())
                .scheduleName(scheduleReqDto.getScheduleName())
                .repeatCycle(scheduleReqDto.getRepeatCycle())
                .dateType(DateType.NORMAL)
                .build();
    }
}
