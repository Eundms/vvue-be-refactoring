package com.exciting.vvue.memory;

import com.exciting.vvue.memory.dto.MemoryPlaceFindDto;
import com.exciting.vvue.memory.dto.res.PlaceMemoryToMapDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface MemoryPlaceApi {

  @Operation(summary = "추억 지도 조회", description = "추억이 있는 장소들을 지도에 표시")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  List<PlaceMemoryToMapDto> searchMemoryPlace(
      @RequestParam(required = false) MemoryPlaceFindDto findCondition);

  @Deprecated
  @Operation(summary = "[TODO] (특정 장소의) 모든 추억 조회", description = "특정 장소의 모든 추억 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  void searchMemoryPlace(@PathVariable Long placeId);
}
