package com.exciting.vvue.memory.model.dto.res;


import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.memory.model.PlaceMemoryImage;
import com.exciting.vvue.picture.model.dto.PictureDto;
import com.exciting.vvue.place.model.Place;
import com.exciting.vvue.place.model.dto.PlaceResDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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
        // 장소 ID
        Map<Long, List<PlaceCommentResDto>> comments = placeMemories.stream()
            .collect(
                Collectors.groupingBy(
                    x-> x.getPlace().getId(),
                    Collectors.mapping(PlaceCommentResDto::from, Collectors.toList())
                )
            );
        // 장소 ID 별 장소 정보
        Map<Long, PlaceResDto> places = new HashMap<>();
        for(PlaceMemory placeMemory: placeMemories){
            places.put(placeMemory.getPlace().getId(),PlaceResDto.from(placeMemory.getPlace()));
        }
        // 장소별 rating
        Map<Long, Float> ratings = placeMemories.stream()
            .collect(
                Collectors.groupingBy(
                    x-> x.getPlace().getId(),
                    Collectors.averagingDouble(x->x.getRating())
                )
            )
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().floatValue()
            ));

//        Map<Long, List<PictureDto>> pictures = placeMemories.stream()
//            .collect(
//                Collectors.groupingBy(
//                    x-> x.getPlace().getId(),
//                    Collectors.mapping(PlaceMemory::getPlaceMemoryImageList,Collectors.toList())
//                )
//            );
        Map<Long, List<PictureDto>> pictures = placeMemories.stream()
            .collect(
                Collectors.groupingBy(
                    x -> x.getPlace().getId(),
                    Collectors.flatMapping(
                        memory -> memory.getPlaceMemoryImageList().stream()
                            .map(x-> PictureDto.from(x.getPicture())),
                        Collectors.toList()
                    )
                )
            );
        List<PlaceMemoryResDto> placeMemoryResDtos = new ArrayList<>();
        for(Long placeId: comments.keySet()){
            placeMemoryResDtos.add(
                PlaceMemoryResDto.builder()
                    .place(places.get(placeId))
                    .allRating(ratings.get(placeId))
                    .pictures(pictures.get(placeId))
                    .comments(comments.get(placeId))
                .build()
            );
        }
        return placeMemoryResDtos;
    }
}
