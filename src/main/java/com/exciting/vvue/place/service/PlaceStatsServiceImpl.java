package com.exciting.vvue.place.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.exciting.vvue.place.PlaceStatsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceStatsServiceImpl implements PlaceStatsService {

	private final PlaceStatsRepository placeStatsRepository;

	@Override
	@Scheduled(cron = "0 0 * * * ?")
	public void updatePlateStats() {
		placeStatsRepository.updateSummary();
	}
}
