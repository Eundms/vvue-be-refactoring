package com.exciting.vvue.schedule;

import com.exciting.vvue.common.annotation.AuthenticatedMarried;
import com.exciting.vvue.common.annotation.AuthenticatedUser;
import com.exciting.vvue.common.dto.MessageResDto;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.schedule.dto.req.CalendarSearchReqDto;
import com.exciting.vvue.schedule.dto.req.SchedulePagingReqDto;
import com.exciting.vvue.schedule.dto.req.ScheduleReqDto;
import com.exciting.vvue.schedule.dto.res.CalendarResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleDailyListResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleDailyResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleIdResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleListResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ScheduleApi {

  @Operation(summary = "일정 상세 조회", description = "해당 일정 상세 정보를 조회한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "403", description = "권한 없음"),
      @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  ScheduleResDto get(
      @PathVariable("scheduleId") Long scheduleId,
      @AuthenticatedMarried Married married);

  @Operation(summary = "일정 등록", description = "일정을 등록한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "403", description = "권한 없음"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  ScheduleIdResDto add(
      @RequestBody ScheduleReqDto scheduleReqDto,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married);

  @Operation(summary = "기념일 생일 등록", description = "부부 가입 시 기념일 생일을 일정으로 등록한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  MessageResDto addMarry(@AuthenticatedMarried Married married);

  @Operation(summary = "일정 수정", description = "해당 일정을 수정한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "403", description = "권한 없음"),
      @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  ScheduleIdResDto modify(
      @PathVariable("scheduleId") Long scheduleId,
      @RequestBody ScheduleReqDto scheduleReqDto,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married);

  @Operation(summary = "일정 삭제", description = "해당 일정을 논리 삭제한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "403", description = "권한 없음"),
      @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  MessageResDto delete(
      @PathVariable("scheduleId") Long scheduleId,
      @AuthenticatedMarried Married married);

  @Operation(summary = "달력 조회", description = "해당 달의 일정 있는 날짜 리스트를 조회한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  CalendarResDto getCalendar(
      @Valid @ModelAttribute CalendarSearchReqDto searchReqDto,
      @AuthenticatedMarried Married married);

  @Operation(summary = "특정 날짜 일정 조회", description = "해당 날짜의 일정 리스트를 조회한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  ScheduleDailyListResDto getDaily(
      @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married);

  @Operation(summary = "D-DAY 일정 조회", description = "예정된 일정 목록을 조회한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  ScheduleListResDto getDday(
      @Valid @ModelAttribute SchedulePagingReqDto pagingReqDto,
      @AuthenticatedMarried Married married);
}