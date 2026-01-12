package com.exciting.vvue.marriage;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import com.exciting.vvue.landing.model.LandingStatus;
import com.exciting.vvue.marriage.dto.MarriedCodeDto;
import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.married.dto.req.MarriedCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/married-code")
public class MarriedCodeController implements MarriedCodeApi {

  private final MarriedCodeService marriedCodeService;
  private final MarriedService marriedService;
  private final SimpMessagingTemplate messagingTemplate;

  @Value("${married-code.length}")
  private int codeLength;

  @Value("${married-code.max-regenerate-attempts}")
  private int maxRegenerateAttempts;

  public MarriedCodeController(MarriedCodeService marriedCodeService,
      MarriedService marriedService,
      SimpMessagingTemplate messagingTemplate) {
    this.marriedCodeService = marriedCodeService;
    this.marriedService = marriedService;
    this.messagingTemplate = messagingTemplate;
  }

  @GetMapping("/generate")
  @Override
  public MarriedCodeDto getMarriedAuthCode() {
    Long userId = AuthContext.getUserId();

    String code = marriedCodeService.generateCode(codeLength, maxRegenerateAttempts);

    if (code == null) {
      throw new VvueException(ErrorCode.MARRIED_CODE_NOT_GENERATED);
    }

    marriedCodeService.addMarriedCode(userId, code);

    return new MarriedCodeDto(code);
  }

  @GetMapping("/regenerate")
  @Override
  public MarriedCodeDto regenerateAuthCode(@RequestParam String marriedCode) {
    Long userId = AuthContext.getUserId();
    String code = marriedCodeService.generateCode(codeLength, maxRegenerateAttempts);
    if (code == null) {
      throw new VvueException(ErrorCode.MARRIED_CODE_NOT_GENERATED);
    }

    if (marriedCodeService.isCodeExists(marriedCode)) {
      marriedCodeService.deleteMarriedCode(marriedCode);
    }

    marriedCodeService.addMarriedCode(userId, code);
    return new MarriedCodeDto(code);
  }

  @PostMapping("/connect")
  @Override
  public void connectMarriedCode(@RequestBody MarriedCodeDto marriedCodeDto) {
    Long userId = AuthContext.getUserId();
    if (!marriedCodeService.isCodeExists(marriedCodeDto.getMarriedCode())) {
      throw new VvueException(ErrorCode.MARRIED_CODE_NOT_GENERATED);
    }

    Long targetId = marriedCodeService.getIdFromMarriedCode(marriedCodeDto.getMarriedCode());

    if (targetId.equals(userId)) {
      throw new VvueException(ErrorCode.MARRIED_WITH_SAME_ID);
    }

    if (marriedService.countByUserId(targetId) > 0 && marriedService.countByUserId(userId) > 0) {
      throw new VvueException(ErrorCode.ALREADY_MARRIED);
    }

    if (marriedCodeService.isCodeExists(marriedCodeDto.getMarriedCode())) {
      marriedCodeService.deleteMarriedCode(marriedCodeDto.getMarriedCode());
    }

    marriedService.createMarried(userId, MarriedCreateDto.builder()
        .partnerId(targetId)
        .build());
    sendMarriedStatus(userId);
    sendMarriedStatus(targetId);
  }

  public void sendMarriedStatus(Long targetUserId) {
    String targetUserTopic = "/topic/user/" + targetUserId + "/married-status";
    messagingTemplate.convertAndSend(targetUserTopic, LandingStatus.CODED.toString().toLowerCase());
  }
}
