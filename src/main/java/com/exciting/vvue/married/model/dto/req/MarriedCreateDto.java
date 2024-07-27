package com.exciting.vvue.married.model.dto.req;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Schema(name = "부부 연결을 위한 정보", description = "배우자 userId, 결혼 기념일")
public class MarriedCreateDto {
	//@ApiModelProperty(value = "배우자 userId", dataType = "Long, int", required = true)
	Long partnerId;
	//@ApiModelProperty(value = "결혼기념일", dataType = "LocalDate", required = true)
	LocalDate marriedDay;

	@Builder
	public MarriedCreateDto(Long partnerId, LocalDate marriedDay) {
		this.partnerId = partnerId;
		this.marriedDay = marriedDay;
	}
}
