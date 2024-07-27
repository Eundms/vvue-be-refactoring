package com.exciting.vvue.married;

import com.exciting.vvue.married.model.dto.res.MarriedInfoExist;
import javax.transaction.Transactional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exciting.vvue.auth.service.AuthService;
import com.exciting.vvue.married.exception.AlreadyMarriedException;
import com.exciting.vvue.married.exception.MarriedInfoNotFoundException;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.model.dto.MarriedDto;
import com.exciting.vvue.married.model.dto.MarriedModifyDto;
import com.exciting.vvue.married.model.dto.req.MarriedCreateDto;
import com.exciting.vvue.married.service.MarriedService;
import com.exciting.vvue.schedule.model.dto.ScheduleReqDto;
import com.exciting.vvue.schedule.service.ScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/married")
@RequiredArgsConstructor
public class MarriedController {
	public final MarriedService marriedService;
	private final AuthService authService;
	private final ScheduleService scheduleService;

	@Operation(summary = "user의 부부정보 가져오기")
	@ApiResponses({
		@ApiResponse(responseCode  = "200", description = "성공", content= {@Content(schema = @Schema(implementation =  MarriedDto.class))}),
		@ApiResponse(responseCode  = "404", description = "부부가 아님")
	})
	@GetMapping("/info")
	public ResponseEntity<?> getMarriedInfo(@RequestHeader("Authorization") String token){
		/**
		 * todo
		 * 부부 정보 가져오기
		 */
		Long id = authService.getUserIdFromToken(token);
		log.debug("[GET] /married/info : id " + id );
		Married marriedInfo = marriedService.getMarriedByUserId(id);
		if(marriedInfo == null)
			throw new MarriedInfoNotFoundException("부부 정보를 가져올 수 없어요.");
		log.debug("[GET] /married/info : id " + marriedInfo );
		MarriedDto married = MarriedDto.from(marriedInfo);
		log.debug("[GET] /married/info : id " + married.toString() );
		return new ResponseEntity<>(married, HttpStatus.OK);
	}


	@PutMapping("/info")
	@Operation(summary = "부부 정보 수정", description = "결혼기념일 미수정시 null로, 사진 미수정시 0 이하의 값으로 보낼 것")
	// @ApiImplicitParam(name = "marriedModifyDto", value = "marriedModifyDto")
	public ResponseEntity<?> updateMarriedInfo(@RequestHeader("Authorization") String token, @RequestBody MarriedModifyDto marriedModifyDto){
		Long id = authService.getUserIdFromToken(token);
		log.debug("[PUT] /married/info : id " + id);

		if(marriedService.getMarriedCount(id) <= 0)
			throw new MarriedInfoNotFoundException("부부 정보를 찾을 수 없어요");

		marriedService.updateMarried(id, marriedModifyDto);

		if(marriedModifyDto.getMarriedDay() != null){
			scheduleService.addAnniversaryAndBirthday(marriedService.getMarriedByUserId(id).getId());
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

//	@Transactional
//	@PostMapping()
//	@Operation(description ="부부 정보 생성", summary = "부부 정보 생성")
//	@Deprecated
//	// @ApiImplicitParam(name = "marriedCreateDto", dataTypeClass = MarriedCreateDto.class, value = "배우자 id, 결혼기념일 ")
//	public ResponseEntity<?> createMarried(@RequestHeader("Authorization") String token, @RequestBody MarriedCreateDto marriedCreateDto){
//		Long id = authService.getUserIdFromToken(token);
//
//		log.debug("[POST] /married : id " + id);
//		log.debug("[POST] /married :  MarriedCreateDto " + marriedCreateDto.toString());
//		if(marriedService.getMarriedCount(marriedCreateDto.getPartnerId()) > 0)
//			throw new AlreadyMarriedException("상대방은 이미 가입중이에요.");
//		marriedService.createMarried(id, marriedCreateDto);
//		return new ResponseEntity<>(HttpStatus.OK);
//	}

	@GetMapping("/is-married")
	@Operation(summary = "부부 정보가 있는지 확인")
	public ResponseEntity<?> isUserMarried(@RequestHeader("Authorization") String token){
		Long userId = authService.getUserIdFromToken(token);
		log.debug("[POST] /married/is-married : id " + userId);
		Married married = marriedService.getMarriedByUserId(userId);

		boolean marriedInfoExists = true;
		if(married==null || married.getFirst()==null || married.getSecond()==null || married.getMarriedDay()==null){
			marriedInfoExists = false;
		}
		return ResponseEntity.status(HttpStatus.OK).body(new MarriedInfoExist(marriedInfoExists));
	}

}
