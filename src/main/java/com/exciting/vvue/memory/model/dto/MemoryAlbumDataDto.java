package com.exciting.vvue.memory.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemoryAlbumDataDto {
    private Long scheduleMemoryId;
    private String pictureUrl;

    public MemoryAlbumDataDto(Long scheduleMemoryId, String pictureUrl) {
        this.scheduleMemoryId = scheduleMemoryId;
        this.pictureUrl = pictureUrl;
    }
}
