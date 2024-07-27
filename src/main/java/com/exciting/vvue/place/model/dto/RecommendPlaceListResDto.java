package com.exciting.vvue.place.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RecommendPlaceListResDto {
    private List<RecommendPlaceResDto> recommendPlaceResDtoList;
    boolean hasNext;
    String lastId;

    @Builder
    public RecommendPlaceListResDto(List<RecommendPlaceResDto> recommendPlaceResDtoList, boolean hasNext, String lastId){
        this.recommendPlaceResDtoList = recommendPlaceResDtoList;
        this.hasNext = hasNext;
        this.lastId = lastId;
    }
}
