package com.exciting.vvue.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.model.dto.UserAuthenticated;
import com.exciting.vvue.user.model.dto.UserDto;
import com.exciting.vvue.user.model.dto.UserModifyDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@Operation(summary = "유저 정보 조회")
	@GetMapping
	public ResponseEntity<UserDto> getUserInfoByToken(
	) {
		Long userId = AuthContext.getUserId();
		log.debug("유저 정보 조회" + userId);
		UserDto userResDto = userService.getUserDto(userId);
		return new ResponseEntity<>(userResDto, HttpStatus.OK);
	}

	@Operation(summary = "유저 정보(성별,생일,닉네임,프로필사진ID) 수정")
	@PutMapping
	public ResponseEntity<?> modify(
		@RequestBody UserModifyDto userModifyDto) {
		Long userId = AuthContext.getUserId();
		userService.modifyUser(userId, userModifyDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "[TODO] 유저 삭제")
	@DeleteMapping
	public ResponseEntity<?> delete() {
		Long userId = AuthContext.getUserId();
		userService.delete(userId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}