package com.exciting.vvue.schedule;

import static com.exciting.vvue.notification.model.EventType.SCHEDULE_CREATE;
import static com.exciting.vvue.notification.model.EventType.SCHEDULE_UPDATED;

import com.exciting.vvue.common.annotation.AuthenticatedMarried;
import com.exciting.vvue.common.annotation.AuthenticatedUser;
import com.exciting.vvue.common.annotation.RequireMarried;
import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.notification.NotificationService;
import com.exciting.vvue.schedule.dto.req.ScheduleReqDto;
import com.exciting.vvue.schedule.dto.res.ScheduleDailyResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleListResDto;
import com.exciting.vvue.schedule.dto.res.ScheduleResDto;
import com.exciting.vvue.schedule.model.Schedule;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequireMarried
public class ScheduleController implements ScheduleApi {

  private final ScheduleService scheduleService;
  private final NotificationService notificationService;

  @GetMapping("/{scheduleId}")
  @Override
  public ScheduleResDto get(
      @PathVariable("scheduleId") Long scheduleId,
      @AuthenticatedMarried Married married) {

    ScheduleResDto scheduleResDto = scheduleService.getSchedule(scheduleId);
    if (!scheduleResDto.getMarriedId().equals(married.getId())) {
      throw new VvueException(ErrorCode.UNAUTHORIZED_USER);
    }

    return scheduleResDto;
  }

  @PostMapping
  @Override
  public Long add(
      @RequestBody ScheduleReqDto scheduleReqDto,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married) {

    Schedule schedule = scheduleService.addSchedule(married, scheduleReqDto);

    Long peerId = married.getPeerId(userId);
    notificationService.sendByToken(
        List.of(peerId),
        SCHEDULE_CREATE,
        new String[]{schedule.getScheduleName()},
        new String[]{schedule.getScheduleName(), schedule.getScheduleDate().toString()}
    );

    return schedule.getId();
  }

  @PostMapping("/marry")
  @Override
  public String addMarry(@AuthenticatedMarried Married married) {
    scheduleService.addAnniversaryAndBirthday(married.getId());
    return "기념일 생일 등록 성공";
  }

  @PutMapping("/{scheduleId}")
  @Override
  public Long modify(
      @PathVariable("scheduleId") Long scheduleId,
      @RequestBody ScheduleReqDto scheduleReqDto,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married) {

    Schedule schedule = scheduleService.modifySchedule(married.getId(), scheduleId, scheduleReqDto);

    Long peerId = married.getPeerId(userId);
    notificationService.sendByToken(
        List.of(peerId),
        SCHEDULE_UPDATED,
        new String[]{schedule.getScheduleName()},
        new String[]{schedule.getScheduleName(), schedule.getScheduleDate().toString()}
    );

    return schedule.getId();
  }

  @DeleteMapping("/{scheduleId}")
  @Override
  public String delete(
      @PathVariable("scheduleId") Long scheduleId,
      @AuthenticatedMarried Married married) {

    if (!scheduleService.existsById(scheduleId)) {
      throw new VvueException(ErrorCode.SCHEDULE_NOT_FOUND);
    }

    scheduleService.deleteSchedule(married.getId(), scheduleId);

    notificationService.sendByToken(
        List.of(married.getId()),
        SCHEDULE_UPDATED,
        null,
        null
    );

    return "일정 삭제 성공";
  }

  @GetMapping("/calendar")
  @Override
  public List<String> getCalendar(
      @RequestParam("year") int year,
      @RequestParam("month") int month,
      @AuthenticatedMarried Married married) {

    return scheduleService.getScheduledDateOnCalendar(married.getId(), year, month);
  }

  @GetMapping("/calendar-daily")
  @Override
  public List<ScheduleDailyResDto> getDaily(
      @RequestParam("date") LocalDate date,
      @AuthenticatedUser Long userId,
      @AuthenticatedMarried Married married) {

    return scheduleService.getScheduleOnDate(married.getId(), userId, date);
  }

  @GetMapping("/dday")
  @Override
  public ScheduleListResDto getDday(
      @RequestParam("idCursor") long idCursor,
      @RequestParam("size") int size,
      @AuthenticatedMarried Married married) {

    return scheduleService.getAllSchedule(married, idCursor, size);
  }
}