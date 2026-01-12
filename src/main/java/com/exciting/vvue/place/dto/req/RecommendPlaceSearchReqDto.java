package com.exciting.vvue.place.dto.req;

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
@Schema(description = "추천 장소 검색 요청")
public class RecommendPlaceSearchReqDto {

  @NotNull
  @Schema(description = "위도", example = "37.5665")
  private Double lat;

  @NotNull
  @Schema(description = "경도", example = "126.9780")
  private Double lng;

  @NotNull
  @Positive
  @Schema(description = "검색 반경 (미터)", example = "5000")
  private Long distance;

  @Schema(description = "페이지 커서", example = "0")
  private Long cursor;

  @NotNull
  @Positive
  @Schema(description = "페이지 크기", example = "10")
  private Long size;
}
