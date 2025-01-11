package com.exciting.vvue.memory.dto.req;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@Builder
public class MemoryAddReqDto {
	@NotNull(message = "[필수] scheduleId")
	private Long scheduleId; //어떤 스캐줄에 대한 추억
	@NotNull(message = "[필수] scheduleName")
	private String scheduleName;
	@NotNull(message = "[필수] scheduleDate")
	private String scheduleDate;

	@NotBlank(message = "[필수] comment")
	@Size(min = 1, max = 300, message = "[범위] comment의 길이 : 1자이상 300자이하")
	private String comment;

	@NotNull(message = "[필수] pictureId")
	private Long pictureId;

	private List<PlaceMemoryReqDto> placeMemories; // 해당 추억에 갔던 장소들

}
