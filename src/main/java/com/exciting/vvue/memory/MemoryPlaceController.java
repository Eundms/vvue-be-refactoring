package com.exciting.vvue.memory;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.auth.AuthService;
import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.memory.model.dto.MemoryPlaceFindDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/memory-place")
@RequiredArgsConstructor
public class MemoryPlaceController {
	private final MemoryPlaceService memoryPlaceService;

	private final AuthService authService;

	@Deprecated
	@Operation(summary = "[TODO] 추억 지도 조회")
	@GetMapping
	public ResponseEntity<?> searchMemoryPlace(@RequestParam(required = false) MemoryPlaceFindDto findCondition) {
		log.debug("[GET] /memory-place");
		Long userId = AuthContext.getUserId();
		List<PlaceMemory> placeMemories = memoryPlaceService.getRecentMemoryPlaceByMarriedId(userId, findCondition);
		return ResponseEntity.ok().body(placeMemories);
	}

	@Deprecated
	@Operation(summary = "[TODO] (특정 장소의) 모든 추억 조회")
	@GetMapping("/{placeId}")
	public ResponseEntity<?> searchMemoryPlace(@PathVariable Long placeId) {
		log.debug("[GET] /memory-place/" + placeId);
		return ResponseEntity.ok().build();
	}
}
