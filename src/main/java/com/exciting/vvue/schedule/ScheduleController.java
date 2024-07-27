package com.exciting.vvue.schedule;

import com.exciting.vvue.auth.exception.UserUnAuthorizedException;
import com.exciting.vvue.auth.service.AuthService;
import com.exciting.vvue.married.exception.MarriedInfoNotFoundException;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.service.MarriedService;
import com.exciting.vvue.notification.model.NotificationContent;
import com.exciting.vvue.notification.model.NotificationType;
import com.exciting.vvue.notification.model.dto.NotificationReqDto;
import com.exciting.vvue.notification.service.NotificationService;
import com.exciting.vvue.schedule.model.Schedule;
import com.exciting.vvue.schedule.model.dto.ScheduleDailyResDto;
import com.exciting.vvue.schedule.model.dto.ScheduleListResDto;
import com.exciting.vvue.schedule.model.dto.ScheduleReqDto;
import com.exciting.vvue.schedule.model.dto.ScheduleResDto;
import com.exciting.vvue.schedule.service.ScheduleService;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.service.UserService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final NotificationService notificationService;
    private final AuthService authService;
    private final MarriedService marriedService;
    private final UserService userService;

    @GetMapping("/{scheduleId}")
    @Operation(summary ="일정 상세 조회", description = "해당 일정 상세 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "403", description = "권한 없음")
            , @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<ScheduleResDto> get(@RequestHeader("Authorization") String token,
                                              @PathVariable("scheduleId") Long scheduleId) {
        log.debug("[GET] /schedules/{}", scheduleId);
        Long marriedId = getMarriedIdWithToken(token);
        ScheduleResDto scheduleResDto = scheduleService.getSchedule(scheduleId);
        if (scheduleResDto.getMarriedId() != marriedId) {
            throw new UserUnAuthorizedException(
                    "[유저]는 해당 [일정]을 볼 권한이 없습니다." + authService.getUserIdFromToken(token) + " " + scheduleId);
        }

        return new ResponseEntity<>(scheduleResDto, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary ="일정 등록", description = "일정을 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "403", description = "권한 없음")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<?> add(@RequestHeader("Authorization") String token,
        @RequestBody ScheduleReqDto scheduleReqDto) {
        log.debug("[POST] /schedules {}", scheduleReqDto);

        Long marriedId = getMarriedIdWithToken(token);
        Schedule schedule = scheduleService.addSchedule(marriedId, scheduleReqDto);

        // 알림 요청
        Map<String, String> data = new HashMap<>();
        data.put("scheduleId", schedule.getId().toString());
        data.put("scheduleDate", schedule.getScheduleDate().toString());
        Long userId = authService.getUserIdFromToken(token);
        Long spouseId = marriedService.getSpouseIdById(userId);
        User spouse = userService.getUserById(spouseId);
        notificationService.sendByToken(
                NotificationReqDto.builder()
                        .targetUserId(spouseId)
                        .content(
                                NotificationContent.builder()
                                        .title("일정 추가")
                                        .body("배우자가 " + "\"" + scheduleReqDto.getScheduleName() + " "
                                                + scheduleReqDto.getScheduleDate().toString() + "\"을 등록했어요")
                                        .image(spouse.getPicture() != null ? spouse.getPicture().getUrl() : null)
                                        .build()
                        )
                        .type(NotificationType.SCHEDULE)
                        //.data(data)
                        .build());
        return new ResponseEntity<>(schedule.getId(), HttpStatus.OK);
    }

    @PostMapping("/marry")
    @Operation(summary ="기념일 생일 등록", description = "부부 가입 시 기념일 생일을 일정으로 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
    })
    public ResponseEntity<String> addMarry(@RequestHeader("Authorization") String token) {
        log.debug("[POST] /schedules/married");
        Long marriedId = getMarriedIdWithToken(token);
        scheduleService.addAnniversaryAndBirthday(marriedId);
        return new ResponseEntity<>("기념일 생일 등록 성공", HttpStatus.OK);
    }

    @PutMapping("/{scheduleId}")
    @Operation(summary ="일정 수정", description = "해당 일정을 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "403", description = "권한 없음")
            , @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<?> modify(@RequestHeader("Authorization") String token,
                                         @PathVariable("scheduleId") Long scheduleId, @RequestBody ScheduleReqDto scheduleReqDto) {
        log.debug("[PUT] /schedules/{} {}", scheduleId, scheduleReqDto);
        Long marriedId = getMarriedIdWithToken(token);
        Schedule schedule = scheduleService.modifySchedule(marriedId, scheduleId, scheduleReqDto);

        // 알림 요청
//        Long userId = authService.getUserIdFromToken(token);
//        Long spouseId = marriedService.getSpouseIdById(userId);
//        User spouse = userService.getUserById(spouseId);
//        Map<String, String> data = new HashMap<>();
//        data.put("scheduleId", schedule.getId().toString());
//        data.put("scheduleDate", schedule.getScheduleDate().toString());
//
//        notificationService.sendByToken(
//                NotificationReqDto.builder()
//                        .targetUserId(spouseId)
//                        .content(
//                                NotificationContent.builder()
//                                        .title("일정 수정")
//                                        .body("배우자가 " + "\"" + scheduleReqDto.getScheduleName() + " "
//                                                + scheduleReqDto.getScheduleDate().toString() + "\"을 수정했어요")
//                                        .image(spouse.getPicture() != null ? spouse.getPicture().getUrl() : null)
//                                        .build()
//                        )//.data(data)
//                        .type(NotificationType.SCHEDULE)
//                        .build());
        return new ResponseEntity<>(schedule.getId(), HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary ="일정 삭제", description = "해당 일정을 논리 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "403", description = "권한 없음")
            , @ApiResponse(responseCode = "404", description = "해당 일정이 존재하지 않음")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String token,
                                         @PathVariable("scheduleId") Long scheduleId) {
        log.debug("[DELETE] /schedules/{}", scheduleId);

        Long marriedId = getMarriedIdWithToken(token);

        ScheduleResDto scheduleReqDto = scheduleService.getSchedule(scheduleId);

        scheduleService.deleteSchedule(marriedId, scheduleId);

        // 알림 요청
//        Long userId = authService.getUserIdFromToken(token);
//        Long spouseId = marriedService.getSpouseIdById(userId);
//        User spouse = userService.getUserById(spouseId);
//        notificationService.sendByToken(
//                NotificationReqDto.builder()
//                        .targetUserId(spouseId)
//                        .content(
//                                NotificationContent.builder()
//                                        .title("일정 삭제")
//                                        .body("배우자가 " + "\"" + scheduleReqDto.getScheduleName() + " "
//                                                + scheduleReqDto.getScheduleDate().toString() + "\"을 삭제했어요")
//                                        .image(spouse.getPicture() != null ? spouse.getPicture().getUrl() : null)
//                                        .build()
//                        )
//                        .type(NotificationType.SCHEDULE)
//                        .build());
        return new ResponseEntity<>("일정 삭제 성공", HttpStatus.OK);
    }

    @GetMapping("/calendar")
    @Operation(summary ="달력 조회", description = "해당 달의 일정 있는 날짜 리스트를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<List<String>> getCalendar(@RequestHeader("Authorization") String token,
                                                    @RequestParam("year") int year, @RequestParam("month") int month) {
        log.debug("[GET] /schedules/calendar {} {}", year, month);

        Long marriedId = getMarriedIdWithToken(token);

        List<String> dateList = scheduleService.getScheduledDateOnCalendar(marriedId, year, month);
        return new ResponseEntity<>(dateList, HttpStatus.OK);
    }

    @GetMapping("/calendar-daily")
    @Operation(summary ="특정 날짜 일정 조회", description = "해당 날짜의 일정 리스트를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<List<ScheduleDailyResDto>> getDaily(
            @RequestHeader("Authorization") String token,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        log.debug("[GET] /schedules/calendar-daily {}", date.toString());

        Long marriedId = getMarriedIdWithToken(token);
        Long userId = authService.getUserIdFromToken(token);

        List<ScheduleDailyResDto> scheduleDailyResDtoList = scheduleService.getScheduleOnDate(marriedId, userId,
                date);
        return new ResponseEntity<>(scheduleDailyResDtoList, HttpStatus.OK);
    }

    @GetMapping("/dday")
    @Operation(summary ="D-DAY 일정 조회", description = "예정된 일정 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공")
            , @ApiResponse(responseCode = "400", description = "잘못된 필드, 값 요청")
            , @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
            , @ApiResponse(responseCode = "500", description = "DB 서버 에러")
    })
    public ResponseEntity<?> getDday(@RequestHeader("Authorization") String token,
                                     @RequestParam("idCursor") long idCursor, @RequestParam("size") int size) {
        log.debug("[GET] /dday ");
        Long marriedId = getMarriedIdWithToken(token);

        ScheduleListResDto scheduleListResDto = scheduleService.getAllSchedule(marriedId,
                idCursor, size);

        return ResponseEntity.ok().body(scheduleListResDto);
    }

    private Long getMarriedIdWithToken(String token) {
        Long userId = authService.getUserIdFromToken(token);
        Married married = marriedService.getMarriedByUserId(userId);
        if (married == null) {
            throw new MarriedInfoNotFoundException("결혼한 정보가 없습니다");
        }
        Long marriedId = married.getId();
        return marriedId;
    }
}
