package com.exciting.vvue.married;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.landing.LandingStateEmitService;
import com.exciting.vvue.landing.model.LandingStatus;
import com.exciting.vvue.common.exception.married.MarriedInfoNotFoundException;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.dto.MarriedDto;
import com.exciting.vvue.married.dto.MarriedModifyDto;
import com.exciting.vvue.married.dto.res.MarriedInfoExist;
import com.exciting.vvue.schedule.ScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/married")
@RequiredArgsConstructor
public class MarriedController {
	public final MarriedService marriedService;
	private final ScheduleService scheduleService;
	private final LandingStateEmitService landingStateEmitService;
	@Operation(summary = "user의 부부정보 가져오기")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공", content = {
			@Content(schema = @Schema(implementation = MarriedDto.class))}),
		@ApiResponse(responseCode = "404", description = "부부가 아님")
	})
	@GetMapping("/info")
	public ResponseEntity<MarriedDto> getMarriedInfo() {
		/**
		 * todo
		 * 부부 정보 가져오기
		 */
		Long userId = AuthContext.getUserId();
		Married marriedInfo = marriedService.getMarriedByUserIdWithDetails(userId);
		if (marriedInfo == null)
			throw new MarriedInfoNotFoundException("부부 정보를 가져올 수 없어요.");
		log.debug("[GET] /married/info : id " + marriedInfo);
		MarriedDto married = MarriedDto.from(marriedInfo);
		log.debug("[GET] /married/info : id " + married.toString());
		return new ResponseEntity<>(married, HttpStatus.OK);
	}

	@PutMapping("/info")
	@Operation(summary = "부부 정보 수정", description = "결혼기념일 미수정시 null로, 사진 미수정시 0 이하의 값으로 보낼 것")
	public ResponseEntity<?> updateMarriedInfo(@RequestBody MarriedModifyDto marriedModifyDto) {
		Long userId = AuthContext.getUserId();

		if (marriedService.getMarriedCount(userId) <= 0)
			throw new MarriedInfoNotFoundException("부부 정보를 찾을 수 없어요");

		marriedService.updateMarried(userId, marriedModifyDto);

		Married married = marriedService.getMarriedByUserid(userId);
		if (marriedModifyDto.getMarriedDay() != null) {
			scheduleService.addAnniversaryAndBirthday(married.getId());
		}

		landingStateEmitService.notifyLandingState(married.getId(), "MARRIED", LandingStatus.COMPLETE);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/is-married")
	@Operation(summary = "부부 정보가 있는지 확인")
	public ResponseEntity<?> isUserMarried() {
		Long userId = AuthContext.getUserId();
		Married married = marriedService.getMarriedByUserid(userId);

		boolean marriedInfoExists = true;
		if (married == null || !married.isAllInfoUpdated()) {
			marriedInfoExists = false;
		}
		return ResponseEntity.status(HttpStatus.OK).body(new MarriedInfoExist(marriedInfoExists));
	}

}
