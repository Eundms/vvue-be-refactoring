package com.exciting.vvue.marriage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.common.exception.married.AlreadyMarriedException;
import com.exciting.vvue.landing.LandingService;
import com.exciting.vvue.landing.model.LandingStatus;
import com.exciting.vvue.landing.model.dto.LandingInfos;
import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.common.exception.married.MarriedCodeNotGeneratedException;
import com.exciting.vvue.common.exception.married.MarriedWithSameIdException;
import com.exciting.vvue.marriage.dto.MarriedCodeDto;
import com.exciting.vvue.married.dto.req.MarriedCreateDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/married-code")
@RequiredArgsConstructor
public class MarriedCodeController {
	private final MarriedCodeService marriedCodeService;
	private final MarriedService marriedService;
	private final SimpMessagingTemplate messagingTemplate;


	private final int REGENERATE_COUNT = 10;
	private final int CODE_LENGTH = 8;

	// 부부 인증 코드 발급
	@GetMapping("/generate")
	@Operation(summary = "부부 인증 코드 발급 (자신 id로)", description = "부부 인증 코드를 발급한다")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공", content = {
			@Content(schema = @Schema(implementation = MarriedCodeDto.class))}),
		@ApiResponse(responseCode = "404", description = "인증코드 생성실패"),
	})
	public ResponseEntity<?> getMarriedAuthCode() {
		Long userId = AuthContext.getUserId();

		String code = marriedCodeService.generateCode(CODE_LENGTH, REGENERATE_COUNT);

		if (code == null)
			throw new MarriedCodeNotGeneratedException("인증 코드 생성에 실패했어요.");

		marriedCodeService.addMarriedCode(userId, code);

		MarriedCodeDto marriedCodeDto = new MarriedCodeDto(code);

		return new ResponseEntity<>(marriedCodeDto, HttpStatus.OK);
	}

	@GetMapping("/regenerate")
	@Operation(summary = "부부 인증 코드 재발급", description = "인증 코드 시간 만료 시 자동 호출 + 버튼 눌렀을 때 호출")
	@Parameter(name = "marriedCode", description = "전에 발급된 부부인증코드", required = true)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공", content = {
			@Content(schema = @Schema(implementation = MarriedCodeDto.class))}),
		@ApiResponse(responseCode = "404", description = "인증코드 생성실패"),
	})
	public ResponseEntity<?> regenerateAuthCode(@RequestParam String marriedCode) {
		/**
		 * todo
		 * 인증코드 재발급
		 * 인증코드 중복처리 - redis
		 * 인증방식 논의 필요
		 */
		Long userId = AuthContext.getUserId();
		String code = marriedCodeService.generateCode(CODE_LENGTH, REGENERATE_COUNT);
		if (code == null)
			throw new MarriedCodeNotGeneratedException("인증 코드 생성에 실패했어요.");

		if (marriedCodeService.isCodeExists(marriedCode))
			marriedCodeService.deleteMarriedCode(marriedCode);

		marriedCodeService.addMarriedCode(userId, code);
		return new ResponseEntity<>(new MarriedCodeDto(code), HttpStatus.OK);
	}

	@PostMapping("/connect")
	@Operation(summary = "인증 코드 일치 확인", description = "인증 코드 발급한 유저와 부부 정보 생성")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공", content = {
			@Content(schema = @Schema(implementation = Long.class))}),
		@ApiResponse(responseCode = "400", description = "자신 인증코드 입력, 이미 부부 정보가 있는 사람과 연동 시도"),
		@ApiResponse(responseCode = "404", description = "redis에 인증 코드 없음"),
	})
	public ResponseEntity<?> connectMarriedCode(@RequestBody MarriedCodeDto marriedCodeDto) {
		Long userId = AuthContext.getUserId();
		if (!marriedCodeService.isCodeExists(marriedCodeDto.getMarriedCode()))
			throw new MarriedCodeNotGeneratedException("인증 코드가 존재하지 않아요.");

		Long targetId = marriedCodeService.getIdFromMarriedCode(marriedCodeDto.getMarriedCode());

		if (targetId == userId)
			throw new MarriedWithSameIdException("혼자선 부부가 될 수 없어요!");

		if (marriedService.countByUserId(targetId) > 0 && marriedService.countByUserId(userId) > 0)
			throw new AlreadyMarriedException("상대방은 이미 가입중이에요.");

		// 코드 있다면 redis에서 지우기
		if (marriedCodeService.isCodeExists(marriedCodeDto.getMarriedCode()))
			marriedCodeService.deleteMarriedCode(marriedCodeDto.getMarriedCode());

		// 부부 정보 연결
		Long marriedId = marriedService.createMarried(userId, MarriedCreateDto.builder()
			.partnerId(targetId)
			.build());
		sendMarriedStatus(userId);
		sendMarriedStatus(targetId);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	public void sendMarriedStatus(Long targetUserId) {
		String targetUserTopic = "/topic/user/" + targetUserId + "/married-status";
		messagingTemplate.convertAndSend(targetUserTopic, LandingStatus.CODED.toString().toLowerCase());
	}
}
