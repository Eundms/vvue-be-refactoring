package com.exciting.vvue.memory.dto.res;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.picture.dto.PictureDto;
import com.exciting.vvue.place.dto.res.PlaceResDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class PlaceMemoryResDto {
	private PlaceResDto place; // 장소
	private Float allRating; // 장소별 별점
	private List<PictureDto> pictures; // 장소별 이미지 리스트

	private List<PlaceCommentResDto> comments;

	@Builder
	public PlaceMemoryResDto(PlaceResDto place, Float allRating, List<PictureDto> pictures,
		List<PlaceCommentResDto> comments) {
		this.place = place;
		this.allRating = allRating;
		this.pictures = pictures;
		this.comments = comments;
	}

	public static List<PlaceMemoryResDto> from(List<PlaceMemory> placeMemories) {
		// 1. 장소 ID 별로 그룹화된 코멘트
		Map<Long, List<PlaceCommentResDto>> comments = placeMemories.stream()
			.collect(
				Collectors.groupingBy(
					x -> x.getPlace().getId(),
					Collectors.mapping(PlaceCommentResDto::from, Collectors.toList())
				)
			);
		// 2. 장소 ID 별로 장소 정보 매핑
		Map<Long, PlaceResDto> places = placeMemories.stream()
			.collect(Collectors.toMap(
				x -> x.getPlace().getId(),
				x -> PlaceResDto.from(x.getPlace()),
				(existing, replacement) -> existing
			));

		// 3. 장소 ID 별로 평균 rating 계산
		Map<Long, Float> ratings = placeMemories.stream()
			.collect(
				Collectors.groupingBy(
					x -> x.getPlace().getId(),
					Collectors.averagingDouble(PlaceMemory::getRating)
				)
			)
			.entrySet()
			.stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> entry.getValue().floatValue()
			));

		// 4. 장소 ID 별로 관련 사진 리스트
		Map<Long, List<PictureDto>> pictures = placeMemories.stream()
			.collect(
				Collectors.groupingBy(
					x -> x.getPlace().getId(),
					Collectors.flatMapping(
						memory -> memory.getPlaceMemoryImageList().stream()
							.map(x -> PictureDto.from(x.getPicture())),
						Collectors.toList()
					)
				)
			);
		// 5. PlaceMemoryResDto 생성
		return comments.keySet().stream()
			.map(placeId -> PlaceMemoryResDto.builder()
				.place(places.get(placeId))
				.allRating(ratings.get(placeId))
				.pictures(pictures.get(placeId))
				.comments(comments.get(placeId))
				.build())
			.collect(Collectors.toList());
	}
}
