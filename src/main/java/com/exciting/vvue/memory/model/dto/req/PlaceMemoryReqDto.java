package com.exciting.vvue.memory.model.dto.req;

import com.exciting.vvue.place.model.dto.PlaceReqDto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceMemoryReqDto {
    private PlaceReqDto place;
    private Float rating;
    private String comment;
    private List<Long> pictureIds;
}
