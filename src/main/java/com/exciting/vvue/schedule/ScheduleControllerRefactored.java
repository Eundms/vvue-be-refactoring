package com.exciting.vvue.schedule;

import static com.exciting.vvue.notification.model.EventType.SCHEDULE_CREATE;
import static com.exciting.vvue.notification.model.EventType.SCHEDULE_UPDATED;

import com.exciting.vvue.common.annotation.AuthenticatedMarried;
import com.exciting.vvue.common.annotation.AuthenticatedUser;
import com.exciting.vvue.common.annotation.RequireMarried;
import com.exciting.vvue.common.exception.schedule.ScheduleNotFoundException;
import com.exciting.vvue.common.exception.user.UserUnAuthorizedException;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.notification.NotificationService;
import com.exciting.vvue.schedule.dto.req.ScheduleReqDto;
import com.exciting.vvue.schedule.dto.res.ScheduleDailyResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleListResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleResDto;
import com.exciting.vvue.schedule.model.Schedule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@RequireMarried  // 클래스 레벨에서 모든 메소드에 결혼 정보 필요함을 명시
public class ScheduleControllerRefactored {

  private final ScheduleService scheduleService;
  private final NotificationService notificationService;

  @GetMapping("/{scheduleId}")
  @Operation(summary = "일정 상세 조회", description = "해당 일정 상세 정보를 조회한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "403", description = "권한 없음"),
      @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  public ResponseEntity<ScheduleResDto> get(
      @PathVariable("scheduleId") Long scheduleId,
      @AuthenticatedMarried Married married) {

    ScheduleResDto scheduleResDto = scheduleService.getSchedule(scheduleId);
    if (!scheduleResDto.getMarriedId().equals(married.getId())) {
      throw new UserUnAuthorizedException("해당 일정을 볼 권한이 없습니다.");
    }

    return ResponseEntity.ok(scheduleResDto);
  }

  @PostMapping
  @Operation(summary = "일정 등록", description = "일정을 등록한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "403", description = "권한 없음"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  public ResponseEntity<Long> add(
      @RequestBody ScheduleReqDto scheduleReqDto,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married) {

    Schedule schedule = scheduleService.addSchedule(married, scheduleReqDto);

    // 배우자에게 알림 전송
    Long peerId = married.getPeerId(userId);
    notificationService.sendByToken(
        List.of(peerId),
        SCHEDULE_CREATE,
        new String[]{schedule.getScheduleName()},
        new String[]{schedule.getScheduleName(), schedule.getScheduleDate().toString()}
    );

    return ResponseEntity.ok(schedule.getId());
  }

  @PostMapping("/marry")
  @Operation(summary = "기념일 생일 등록", description = "부부 가입 시 기념일 생일을 일정으로 등록한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
  })
  public ResponseEntity<String> addMarry(@AuthenticatedMarried Married married) {
    scheduleService.addAnniversaryAndBirthday(married.getId());
    return ResponseEntity.ok("기념일 생일 등록 성공");
  }

  @PutMapping("/{scheduleId}")
  @Operation(summary = "일정 수정", description = "해당 일정을 수정한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "403", description = "권한 없음"),
      @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  public ResponseEntity<Long> modify(
      @PathVariable("scheduleId") Long scheduleId,
      @RequestBody ScheduleReqDto scheduleReqDto,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married) {

    Schedule schedule = scheduleService.modifySchedule(married.getId(), scheduleId, scheduleReqDto);

    // 배우자에게 알림 전송
    Long peerId = married.getPeerId(userId);
    notificationService.sendByToken(
        List.of(peerId),
        SCHEDULE_UPDATED,
        new String[]{schedule.getScheduleName()},
        new String[]{schedule.getScheduleName(), schedule.getScheduleDate().toString()}
    );

    return ResponseEntity.ok(schedule.getId());
  }

  @DeleteMapping("/{scheduleId}")
  @Operation(summary = "일정 삭제", description = "해당 일정을 논리 삭제한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "403", description = "권한 없음"),
      @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  public ResponseEntity<String> delete(
      @PathVariable("scheduleId") Long scheduleId,
      @AuthenticatedMarried Married married) {

    if (!scheduleService.existsById(scheduleId)) {
      throw new ScheduleNotFoundException("해당 일정이 존재하지 않습니다.");
    }

    scheduleService.deleteSchedule(married.getId(), scheduleId);

    // 배우자에게 알림 전송
    notificationService.sendByToken(
        List.of(married.getId()),
        SCHEDULE_UPDATED,
        null,
        null
    );

    return ResponseEntity.ok("일정 삭제 성공");
  }

  @GetMapping("/calendar")
  @Operation(summary = "달력 조회", description = "해당 달의 일정 있는 날짜 리스트를 조회한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  public ResponseEntity<List<String>> getCalendar(
      @RequestParam("year") int year,
      @RequestParam("month") int month,
      @AuthenticatedMarried Married married) {

    List<String> dateList = scheduleService.getScheduledDateOnCalendar(married.getId(), year, month);
    return ResponseEntity.ok(dateList);
  }

  @GetMapping("/calendar-daily")
  @Operation(summary = "특정 날짜 일정 조회", description = "해당 날짜의 일정 리스트를 조회한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  public ResponseEntity<List<ScheduleDailyResDto>> getDaily(
      @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married) {

    List<ScheduleDailyResDto> scheduleDailyResDtoList = scheduleService.getScheduleOnDate(
        married.getId(), userId, date);
    return ResponseEntity.ok(scheduleDailyResDtoList);
  }

  @GetMapping("/dday")
  @Operation(summary = "D-DAY 일정 조회", description = "예정된 일정 목록을 조회한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요청 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청"),
      @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자"),
      @ApiResponse(responseCode = "500", description = "DB 서버 에러")
  })
  public ResponseEntity<ScheduleListResDto> getDday(
      @RequestParam("idCursor") long idCursor,
      @RequestParam("size") int size,
      @AuthenticatedMarried Married married) {

    ScheduleListResDto scheduleResDtoList = scheduleService.getAllSchedule(married, idCursor, size);
    return ResponseEntity.ok(scheduleResDtoList);
  }
}