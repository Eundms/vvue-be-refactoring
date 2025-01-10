package com.exciting.vvue.place.dto.req;

import java.util.List;

import com.exciting.vvue.place.dto.req.PlaceReqDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
