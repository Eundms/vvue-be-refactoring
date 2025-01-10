package com.exciting.vvue.memory.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.exciting.vvue.memory.MemoryPlaceService;
import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.memory.model.ScheduleMemory;
import com.exciting.vvue.memory.dto.MemoryPlaceFindDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MemoryPlaceServiceImpl implements MemoryPlaceService {
	private final ScheduleMemoryRepository scheduleMemoryRepository;

	@Override
	public List<PlaceMemory> getRecentMemoryPlaceByMarriedId(Long marriedId, MemoryPlaceFindDto findCondition) {
		List<ScheduleMemory> scheduleMemories = scheduleMemoryRepository.findAllByMarriedId(marriedId);
		// TODO: x,y 위치 근처 장소
		BigDecimal x, y;
		if (findCondition != null && findCondition.getX() != null && findCondition.getY() != null) {
			x = findCondition.getX();
			y = findCondition.getY();
		} else {
			// TODO: 가장 최근에 갔던 장소
			// List<PlaceMemory> places;

			// TODO: 근처에 있는 장소들을 리턴
			//
		}

		return new ArrayList<>();
	}
}
