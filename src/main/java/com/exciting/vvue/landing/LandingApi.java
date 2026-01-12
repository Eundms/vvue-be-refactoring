package com.exciting.vvue.landing;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface LandingApi {

  @Operation(summary = "모든 정보(부부연결유무/유저정보-성별,생일,닉네임) 입력 여부 확인", description = "랜딩 페이지 상태 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "500", description = "서버 에러")
  })
  String getLandingState();
}
