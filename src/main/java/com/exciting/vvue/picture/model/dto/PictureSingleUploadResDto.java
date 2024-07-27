package com.exciting.vvue.picture.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PictureSingleUploadResDto {
	MetaReqDto meta;
	Long pictureId;
	@Builder
	public PictureSingleUploadResDto(MetaReqDto meta, Long id){
		this.meta = meta;
		this.pictureId = id;
	}
}
