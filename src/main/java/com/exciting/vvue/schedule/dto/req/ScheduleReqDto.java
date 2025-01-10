package com.exciting.vvue.schedule.dto.req;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.exciting.vvue.schedule.model.RepeatCycle;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ScheduleReqDto {
	@NotNull(message = "[필수] yyyy-mm-dd 형식")
	private LocalDate scheduleDate;
	@NotBlank(message = "[필수] scheduleName(1-30)")
	private String scheduleName;
	@NotNull(message = "[필수] repeatCycle(NONREPEAT, MONTHLY, YEARLY")
	private RepeatCycle repeatCycle;

	public ScheduleReqDto(LocalDate scheduleDate, String scheduleName, RepeatCycle repeatCycle) {
		this.scheduleDate = scheduleDate;
		this.scheduleName = scheduleName;
		this.repeatCycle = repeatCycle;
	}
}
