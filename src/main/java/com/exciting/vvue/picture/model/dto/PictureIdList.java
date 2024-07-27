package com.exciting.vvue.picture.model.dto;

import java.util.List;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PictureIdList {
	private List<Long> pictureIds;

	@Builder
	public PictureIdList(List<Long> pictureIds){
		this.pictureIds = pictureIds;
	}
}
