package com.exciting.vvue.memory.model.dto.res;

import com.exciting.vvue.memory.model.dto.MemoryAlbumDataDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class MemoryAlbumResDto {
    private List<MemoryAlbumDataDto> allMemories;
    private Long lastCursorId;
    private boolean hasNext;
    @Builder
    public MemoryAlbumResDto(List<MemoryAlbumDataDto> allMemories, Long lastCursorId,
        boolean hasNext) {
        this.allMemories = allMemories;
        this.lastCursorId = lastCursorId;
        this.hasNext = hasNext;
    }
}
