package com.exciting.vvue.schedule.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.schedule.dto.req.ScheduleReqDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
	public Schedule(Long id, Married married, LocalDate scheduleDate, String scheduleName, RepeatCycle repeatCycle,
		DateType dateType) {
		this.id = id;
		this.married = married;
		this.scheduleDate = scheduleDate;
		this.scheduleName = scheduleName;
		this.repeatCycle = repeatCycle;
		this.dateType = dateType;
	}

	public static Schedule marryAll(Married married, LocalDate date, DateType dateType) {
		return Schedule.builder()
			.married(married)
			.scheduleName(dateType.getDescription())
			.scheduleDate(date)
			.repeatCycle(RepeatCycle.YEARLY)
			.dateType(dateType)
			.build();
	}

	public static Schedule from(ScheduleReqDto scheduleReqDto, Married married) {
		return Schedule.builder()
			.married(married)
			.scheduleDate(scheduleReqDto.getScheduleDate())
			.scheduleName(scheduleReqDto.getScheduleName())
			.repeatCycle(scheduleReqDto.getRepeatCycle())
			.dateType(DateType.NORMAL)
			.build();
	}

	public void updateSchedule(ScheduleReqDto scheduleReqDto) {
		this.scheduleDate = scheduleReqDto.getScheduleDate();
		this.scheduleName = scheduleReqDto.getScheduleName();
		this.repeatCycle = scheduleReqDto.getRepeatCycle();
	}
}
