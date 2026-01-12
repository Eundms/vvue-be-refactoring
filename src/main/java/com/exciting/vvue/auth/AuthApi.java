package com.exciting.vvue.auth;

import com.exciting.vvue.auth.dto.req.SocialUserReqDto;
import com.exciting.vvue.auth.dto.res.AuthResDto;
import com.exciting.vvue.auth.jwt.exception.InvalidTokenException;
import com.exciting.vvue.auth.jwt.model.JwtDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AuthApi {

  @Operation(summary = "로그인/회원 가입", description = "토큰 발급 됨")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "500", description = "서버 에러")
  })
  AuthResDto loginOrRegister(
      @RequestBody SocialUserReqDto socialUserReqDto,
      HttpServletResponse response);

  @Operation(summary = "accessToken 재발급", description = "refresh-token 헤더로 accessToken 재발급")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰"),
      @ApiResponse(responseCode = "500", description = "서버 에러")
  })
  JwtDto refreshToken(
      @RequestHeader("refresh-token") String refreshToken) throws InvalidTokenException;
}
