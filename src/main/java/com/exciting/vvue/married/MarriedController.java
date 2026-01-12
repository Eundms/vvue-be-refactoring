package com.exciting.vvue.married;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import com.exciting.vvue.landing.model.LandingStatus;
import com.exciting.vvue.married.dto.MarriedDto;
import com.exciting.vvue.married.dto.MarriedModifyDto;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/married")
@RequiredArgsConstructor
public class MarriedController implements MarriedApi {

  public final MarriedService marriedService;
  private final ScheduleService scheduleService;
  private final SimpMessagingTemplate messagingTemplate;

  @GetMapping("/info")
  @Override
  public MarriedDto getMarriedInfo() {
    Long userId = AuthContext.getUserId();
    Married marriedInfo = marriedService.getMarriedByUserIdWithDetails(userId);
    if (marriedInfo == null) {
      throw new VvueException(ErrorCode.MARRIED_INFO_NOT_FOUND);
    }
    return MarriedDto.from(marriedInfo);
  }

  @PutMapping("/info")
  @Override
  public void updateMarriedInfo(@RequestBody MarriedModifyDto marriedModifyDto) {
    Long userId = AuthContext.getUserId();
    Long marriedId = marriedService.updateMarriedAndReturnId(userId, marriedModifyDto);
    if (marriedModifyDto.getMarriedDay() != null) {
      scheduleService.addAnniversaryAndBirthday(marriedId);
    }

    sendMarriedStatus(userId);
    Long spouseId = marriedService.getSpouseId(userId);
    sendMarriedStatus(spouseId);
  }

  public void sendMarriedStatus(Long targetUserId) {
    String targetUserTopic = "/topic/user/" + targetUserId + "/married-status";
    messagingTemplate.convertAndSend(targetUserTopic,
        LandingStatus.COMPLETE.toString().toLowerCase());
  }
}
