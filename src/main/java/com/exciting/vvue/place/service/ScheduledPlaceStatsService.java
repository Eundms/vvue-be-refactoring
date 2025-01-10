package com.exciting.vvue.place.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduledPlaceStatsService {

	private final PlaceStatsRepository placeStatsRepository;

	@Scheduled(cron = "0 0 * * * ?")
	public void updatePlateStats() {
		placeStatsRepository.updateSummary();
	}
}
