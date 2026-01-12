package com.exciting.vvue.develop;

import com.exciting.vvue.auth.jwt.model.JwtDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface DevelopApi {

  @Operation(summary = "강제 결혼(코드인증까지만 완료/추가 정보 입력 필요함)", description = "개발 테스트용 강제 결혼")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "결혼 성공"),
      @ApiResponse(responseCode = "204", description = "결혼 실패")
  })
  String forceMarry(@PathVariable Long spouseId);

  @Operation(summary = "강제 이혼 (스케줄/추억이 없는 경우만 가능)", description = "개발 테스트용 강제 이혼")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "이혼 성공")
  })
  String divorce();

  @Deprecated
  @Operation(summary = "데이터 입력 : 개발 테스트", description = "[사진만 DB에 미리 업로드하기]유저(1-8),결혼 1~4, 결혼일정/일반달반복일정/반복없음(1-9, 각 유저마다3가지), 추억(스케줄에 대해 홀수번 유저만 작성), 알림(1-6 유저의 결혼/달반복/반복없음에 존재)")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "테스트 데이터 생성 성공")
  })
  void testGenearte();

  @Operation(summary = "테스트 JWT 토큰 발급 (유저정보 확인하기): providerId = {providerId}", description = "개발 테스트용 토큰 발급")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
  })
  JwtDto testTokenGen(@PathVariable Long providerId);

  @Operation(summary = "덤프 장소 데이터 추가", description = "카카오 API를 통한 장소 데이터 추가")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "데이터 생성 성공"),
      @ApiResponse(responseCode = "400", description = "Jackson 에러")
  })
  String dumpPlaceData(@RequestHeader("authUserId") Long userId);

  @Operation(summary = "덤프 장소 데이터 추가(param 추가)", description = "위치, 코드 지정하여 장소 데이터 추가")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "데이터 생성 성공"),
      @ApiResponse(responseCode = "400", description = "Jackson 에러")
  })
  String selectDumpPlaceData(
      @RequestHeader("authUserId") Long userId,
      @RequestParam("x") Double x,
      @RequestParam("y") Double y,
      @RequestParam("code") String categoryCode);
}
