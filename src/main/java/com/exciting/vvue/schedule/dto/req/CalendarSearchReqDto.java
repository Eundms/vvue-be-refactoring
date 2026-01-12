package com.exciting.vvue.schedule.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "달력 조회 요청")
public class CalendarSearchReqDto {

  @NotNull
  @Min(1900)
  @Max(2100)
  @Schema(description = "연도", example = "2024")
  private Integer year;

  @NotNull
  @Min(1)
  @Max(12)
  @Schema(description = "월", example = "1")
  private Integer month;
}
