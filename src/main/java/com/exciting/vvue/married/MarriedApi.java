package com.exciting.vvue.married;

import com.exciting.vvue.married.dto.MarriedDto;
import com.exciting.vvue.married.dto.MarriedModifyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

public interface MarriedApi {

  @Operation(summary = "user의 부부정보 가져오기", description = "현재 로그인한 유저의 부부 정보 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공", content = {
          @Content(schema = @Schema(implementation = MarriedDto.class))}),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "404", description = "부부가 아님")
  })
  MarriedDto getMarriedInfo();

  @Operation(summary = "부부 정보 수정", description = "결혼기념일 미수정시 null로, 사진 미수정시 0 이하의 값으로 보낼 것")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "404", description = "부부 정보를 찾을 수 없음")
  })
  void updateMarriedInfo(@RequestBody MarriedModifyDto marriedModifyDto);
}
