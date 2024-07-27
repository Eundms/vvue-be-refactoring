package com.exciting.vvue.place.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDumpReqDto {
    private List<PlaceReqDto> documents;

    public List<PlaceReqDto> getDocuments() {
        return documents;
    }

    public void setDocuments(List<PlaceReqDto> documents) {
        this.documents = documents;
    }
}
