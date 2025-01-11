package com.exciting.vvue.schedule;

import java.time.LocalDate;
import java.util.List;

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

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.common.exception.married.MarriedInfoNotFoundException;
import com.exciting.vvue.common.exception.schedule.ScheduleNotFoundException;
import com.exciting.vvue.common.exception.user.UserUnAuthorizedException;
import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.schedule.dto.req.ScheduleReqDto;
import com.exciting.vvue.schedule.dto.res.ScheduleDailyResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleListResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleResDto;
import com.exciting.vvue.schedule.model.Schedule;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

	private final ScheduleService scheduleService;
	private final MarriedService marriedService;

	@GetMapping("/{scheduleId}")
	@Operation(summary = "일정 상세 조회", description = "해당 일정 상세 정보를 조회한다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "요청 성공")
		, @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
		, @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
		, @ApiResponse(responseCode = "403", description = "권한 없음")
		, @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음")
		, @ApiResponse(responseCode = "500", description = "DB 서버 에러")
	})
	public ResponseEntity<ScheduleResDto> get(
		@PathVariable("scheduleId") Long scheduleId) {
		Long userId = AuthContext.getUserId();
		Married married = getMarriedIdWith(userId);
		ScheduleResDto scheduleResDto = scheduleService.getSchedule(scheduleId);
		if (scheduleResDto.getMarriedId() != married.getId()) {
			throw new UserUnAuthorizedException(
				"[유저]는 해당 [일정]을 볼 권한이 없습니다." + userId + " " + scheduleId);
		}

		return new ResponseEntity<>(scheduleResDto, HttpStatus.OK);
	}

	@PostMapping
	@Operation(summary = "일정 등록", description = "일정을 등록한다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "요청 성공")
		, @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
		, @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
		, @ApiResponse(responseCode = "403", description = "권한 없음")
		, @ApiResponse(responseCode = "500", description = "DB 서버 에러")
	})
	public ResponseEntity<?> add(
		@RequestBody ScheduleReqDto scheduleReqDto) {
		Long userId = AuthContext.getUserId();
		Married married = getMarriedIdWith(userId);
		Schedule schedule = scheduleService.addSchedule(married, scheduleReqDto);

		// 알림 요청
		// 배우자가 일정을 등록했어요
		return new ResponseEntity<>(schedule.getId(), HttpStatus.OK);
	}

	@PostMapping("/marry")
	@Operation(summary = "기념일 생일 등록", description = "부부 가입 시 기념일 생일을 일정으로 등록한다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "요청 성공")
		, @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
		, @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
	})
	public ResponseEntity<String> addMarry() {
		Long userId = AuthContext.getUserId();
		Married married = getMarriedIdWith(userId);
		scheduleService.addAnniversaryAndBirthday(married.getId());
		return new ResponseEntity<>("기념일 생일 등록 성공", HttpStatus.OK);
	}

	@PutMapping("/{scheduleId}")
	@Operation(summary = "일정 수정", description = "해당 일정을 수정한다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "요청 성공")
		, @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
		, @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
		, @ApiResponse(responseCode = "403", description = "권한 없음")
		, @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음")
		, @ApiResponse(responseCode = "500", description = "DB 서버 에러")
	})
	public ResponseEntity<?> modify(
		@PathVariable("scheduleId") Long scheduleId, @RequestBody ScheduleReqDto scheduleReqDto) {
		Long userId = AuthContext.getUserId();

		Married married = getMarriedIdWith(userId);
		Schedule schedule = scheduleService.modifySchedule(married.getId(), scheduleId, scheduleReqDto);

		return new ResponseEntity<>(schedule.getId(), HttpStatus.OK);
	}

	@DeleteMapping("/{scheduleId}")
	@Operation(summary = "일정 삭제", description = "해당 일정을 논리 삭제한다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "요청 성공")
		, @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
		, @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
		, @ApiResponse(responseCode = "403", description = "권한 없음")
		, @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음")
		, @ApiResponse(responseCode = "500", description = "DB 서버 에러")
	})
	public ResponseEntity<String> delete(
		@PathVariable("scheduleId") Long scheduleId) {
		Long userId = AuthContext.getUserId();

		Married married = getMarriedIdWith(userId);

		boolean isExists = scheduleService.existsById(scheduleId);
		if(!isExists) throw new ScheduleNotFoundException("스케줄 없음");

		scheduleService.deleteSchedule(married.getId(), scheduleId);
		return new ResponseEntity<>("일정 삭제 성공", HttpStatus.OK);
	}

	@GetMapping("/calendar")
	@Operation(summary = "달력 조회", description = "해당 달의 일정 있는 날짜 리스트를 조회한다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "요청 성공")
		, @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
		, @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
		, @ApiResponse(responseCode = "500", description = "DB 서버 에러")
	})
	public ResponseEntity<List<String>> getCalendar(
		@RequestParam("year") int year, @RequestParam("month") int month) {
		Long userId = AuthContext.getUserId();

		Married married = getMarriedIdWith(userId);
		List<String> dateList = scheduleService.getScheduledDateOnCalendar(married.getId(), year, month);
		return new ResponseEntity<>(dateList, HttpStatus.OK);
	}

	@GetMapping("/calendar-daily")
	@Operation(summary = "특정 날짜 일정 조회", description = "해당 날짜의 일정 리스트를 조회한다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "요청 성공")
		, @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
		, @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
		, @ApiResponse(responseCode = "500", description = "DB 서버 에러")
	})
	public ResponseEntity<List<ScheduleDailyResDto>> getDaily(

		@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		Long userId = AuthContext.getUserId();

		Married married = getMarriedIdWith(userId);
		List<ScheduleDailyResDto> scheduleDailyResDtoList = scheduleService.getScheduleOnDate(married.getId(), userId,
			date);
		return new ResponseEntity<>(scheduleDailyResDtoList, HttpStatus.OK);
	}

	@GetMapping("/dday")
	@Operation(summary = "D-DAY 일정 조회", description = "예정된 일정 목록을 조회한다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "요청 성공")
		, @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
		, @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
		, @ApiResponse(responseCode = "500", description = "DB 서버 에러")
	})
	public ResponseEntity<ScheduleListResDto> getDday(
		@RequestParam("idCursor") long idCursor, @RequestParam("size") int size) {
		Long userId = AuthContext.getUserId();

		Married married = getMarriedIdWith(userId);
		ScheduleListResDto scheduleResDtoList = scheduleService.getAllSchedule(married,
			idCursor, size);

		return ResponseEntity.ok().body(scheduleResDtoList);
	}

	private Married getMarriedIdWith(Long userId) {
		Married married = marriedService.getMarriedByUserId(userId);
		if (married == null) {
			throw new MarriedInfoNotFoundException("결혼한 정보가 없습니다");
		}
		return married;
	}
}
