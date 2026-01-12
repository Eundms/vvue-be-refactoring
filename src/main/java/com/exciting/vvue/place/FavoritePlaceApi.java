package com.exciting.vvue.place;

import com.exciting.vvue.place.dto.req.PlaceReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface FavoritePlaceApi {

  @Operation(summary = "즐겨찾기 상태 변경", description = "즐겨찾기의 상태를 변경한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "202", description = "요청 성공, place 등록 필요"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  boolean changeScrap(@RequestParam Long mapPlaceId);

  @Operation(summary = "장소 등록 및 즐겨찾기 하기", description = "장소를 등록하고 즐겨찾기한 상태로 변경.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  String scrapAndAdd(@RequestBody PlaceReqDto placeReqDto);
}
