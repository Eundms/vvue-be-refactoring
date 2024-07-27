package com.exciting.vvue.place.model.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PlaceReqDto {
    private String id ;
    private String address_name;
    private String category_group_code;
    private String category_group_name;
    private String category_name;
    private String distance;
    private String phone;
    private String place_name;
    private String place_url;
    private String road_address_name;
    private String x;
    private String y;

    @Builder
    public PlaceReqDto(String id, String address_name, String category_group_code,
                       String category_group_name, String category_name, String distance,
                       String phone, String place_name,
                       String place_url, String road_address_name, String x, String y) {
        this.id = id;
        this.address_name = address_name;
        this.category_group_code = category_group_code;
        this.category_group_name = category_group_name;
        this.category_name = category_name;
        this.distance = distance;
        this.phone = phone;
        this.place_name = place_name;
        this.place_url = place_url;
        this.road_address_name = road_address_name;
        this.x = x;
        this.y = y;
    }
}
