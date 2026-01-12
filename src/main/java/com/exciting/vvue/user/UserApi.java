package com.exciting.vvue.user;

import com.exciting.vvue.user.dto.UserDto;
import com.exciting.vvue.user.dto.UserModifyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserApi {

  @Operation(summary = "유저 정보 조회", description = "현재 로그인한 유저 정보 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 에러")
  })
  UserDto getUserInfoByToken();

  @Operation(summary = "유저 정보(성별,생일,닉네임,프로필사진ID) 수정", description = "유저 정보 수정")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 에러")
  })
  void modify(@RequestBody UserModifyDto userModifyDto);

  @Operation(summary = "[TODO] 유저 삭제", description = "유저 탈퇴 처리")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 에러")
  })
  void delete();
}
