package com.exciting.vvue.schedule.model.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleListResDto {
	List<ScheduleResDto> scheduleResDtoList;
	boolean hasNext;
	Long lastId;

	@Builder
	public ScheduleListResDto(List<ScheduleResDto> scheduleResDtoList, boolean hasNext, Long lastId) {
		this.scheduleResDtoList = scheduleResDtoList;
		this.hasNext = hasNext;
		this.lastId = lastId;
	}
}
