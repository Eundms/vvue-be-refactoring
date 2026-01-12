package com.exciting.vvue.schedule.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "일정 페이징 요청")
public class SchedulePagingReqDto {

  @Schema(description = "ID 커서 (첫 요청 시 -1)", example = "-1")
  private Long idCursor;

  @NotNull
  @Positive
  @Schema(description = "페이지 크기", example = "10")
  private Integer size;
}
