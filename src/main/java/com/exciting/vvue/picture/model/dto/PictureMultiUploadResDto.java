package com.exciting.vvue.picture.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PictureMultiUploadResDto {
	private MetaReqDto meta;
	private List<Long> pictureIdList;

	@Builder
	public PictureMultiUploadResDto(MetaReqDto meta, List<Long> pictureIdList){
		this.meta = meta;
		this.pictureIdList = pictureIdList;
	}
}
