package com.exciting.vvue.picture.model.dto;

import com.exciting.vvue.picture.model.Picture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class PictureDto {
    private Long id;
    private String url;

    @Builder
    public PictureDto(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public static PictureDto from(Picture picture) {
        return PictureDto.builder()
                .id(picture.getId())
                .url(picture.getUrl())
                .build();
    }
}
