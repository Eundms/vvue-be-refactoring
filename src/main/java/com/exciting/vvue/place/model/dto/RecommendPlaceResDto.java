package com.exciting.vvue.place.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendPlaceResDto {
    private String id;
    private String addressName;
    private String categoryGroupCode;
    private String categoryGroupName;
    private String categoryName;
    private String phone;
    private String placeName;
    private String placeUrl;
    private String roadAddressName;
    private String x;
    private String y;
    private String avgRating;
    private String visitCount;
    private String distance;
    private Boolean favorite;

    @Builder
    public RecommendPlaceResDto(String id, String addressName, String categoryGroupCode,
                                String categoryGroupName, String categoryName, String phone,
                                String placeName,
                                String placeUrl, String roadAddressName, String x, String y,
                                String avgRating, String visitCount, String distance, Boolean favorite){
        this.id = id;
        this.addressName = addressName;
        this.categoryGroupCode = categoryGroupCode;
        this.categoryGroupName = categoryGroupName;
        this.categoryName = categoryName;
        this.phone = phone;
        this.placeName = placeName;
        this.placeUrl = placeUrl;
        this.roadAddressName = roadAddressName;
        this.x = x;
        this.y = y;
        this.avgRating = avgRating;
        this.visitCount = visitCount;
        this.distance = distance;
        this.favorite = favorite;
    }
}
