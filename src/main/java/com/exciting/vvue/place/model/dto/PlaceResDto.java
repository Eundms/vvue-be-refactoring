package com.exciting.vvue.place.model.dto;

import com.exciting.vvue.place.model.Place;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlaceResDto {

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

    @Builder
    public PlaceResDto(String id, String addressName, String categoryGroupCode,
        String categoryGroupName, String categoryName, String phone,
        String placeName,
        String placeUrl, String roadAddressName, String x, String y) {
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
    }

    public static PlaceResDto from(Place place) {
        return PlaceResDto.builder()
            .id(String.valueOf(place.getId()))
            .addressName(place.getAddressName())
            .categoryGroupCode(place.getCategoryGroupCode())
            .categoryGroupName(place.getCategoryGroupName())
            .categoryName(place.getCategoryName())
            .phone(place.getPhone())
            .placeName(place.getPlaceName())
            .placeUrl(place.getPlaceUrl())
            .roadAddressName(place.getAddressName())
            .x(place.getX().toString())
            .y(place.getY().toString())
            .build();
    }
}
