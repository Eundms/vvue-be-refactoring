package com.exciting.vvue.marriage;

import com.exciting.vvue.marriage.dto.MarriedCodeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface MarriedCodeApi {

  @Operation(summary = "부부 인증 코드 발급 (자신 id로)", description = "부부 인증 코드를 발급한다")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공", content = {
          @Content(schema = @Schema(implementation = MarriedCodeDto.class))}),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "404", description = "인증코드 생성실패")
  })
  MarriedCodeDto getMarriedAuthCode();

  @Operation(summary = "부부 인증 코드 재발급", description = "인증 코드 시간 만료 시 자동 호출 + 버튼 눌렀을 때 호출")
  @Parameter(name = "marriedCode", description = "전에 발급된 부부인증코드", required = true)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공", content = {
          @Content(schema = @Schema(implementation = MarriedCodeDto.class))}),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "404", description = "인증코드 생성실패")
  })
  MarriedCodeDto regenerateAuthCode(@RequestParam String marriedCode);

  @Operation(summary = "인증 코드 일치 확인", description = "인증 코드 발급한 유저와 부부 정보 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "400", description = "자신 인증코드 입력, 이미 부부 정보가 있는 사람과 연동 시도"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "404", description = "redis에 인증 코드 없음")
  })
  void connectMarriedCode(@RequestBody MarriedCodeDto marriedCodeDto);
}
