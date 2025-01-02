package com.exciting.vvue.place.model.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendPlaceListResDto {
	boolean hasNext;
	String lastId;
	private List<RecommendPlaceResDto> recommendPlaceResDtoList;

	@Builder
	public RecommendPlaceListResDto(List<RecommendPlaceResDto> recommendPlaceResDtoList, boolean hasNext,
		String lastId) {
		this.recommendPlaceResDtoList = recommendPlaceResDtoList;
		this.hasNext = hasNext;
		this.lastId = lastId;
	}
}
