package com.exciting.vvue.place.model;

import com.exciting.vvue.place.model.dto.PlaceReqDto;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Place {

    @Id
    private Long id;
    private String addressName;
    private String categoryGroupCode;
    private String categoryGroupName;
    private String categoryName;
    private String phone;
    private String placeName;
    private String placeUrl;
    private String roadAddressName;
    @Column(precision = 20, scale = 17)
    private BigDecimal x;
    @Column(precision = 20, scale = 17)
    private BigDecimal y;

    @Builder
    public Place(Long id, String addressName, String categoryGroupCode,
        String categoryGroupName, String categoryName,  String phone,
        String placeName,
        String placeUrl, String roadAddressName, BigDecimal x, BigDecimal y) {
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

    public static Place from(PlaceReqDto placeReqDto) {
        return Place.builder()
            .id(Long.parseLong(placeReqDto.getId()))
            .addressName(placeReqDto.getAddress_name())
            .categoryGroupCode(placeReqDto.getCategory_group_code())
            .categoryGroupName(placeReqDto.getCategory_group_name())
            .categoryName(placeReqDto.getCategory_name())
            .phone(placeReqDto.getPhone())
            .placeName(placeReqDto.getPlace_name())
            .placeUrl(placeReqDto.getPlace_url())
            .roadAddressName(placeReqDto.getRoad_address_name())
            .x(new BigDecimal(placeReqDto.getX()))
            .y(new BigDecimal(placeReqDto.getY()))
            .build();
    }
}
