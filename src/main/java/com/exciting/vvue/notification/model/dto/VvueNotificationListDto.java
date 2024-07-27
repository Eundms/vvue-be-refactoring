package com.exciting.vvue.notification.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class VvueNotificationListDto {
    private List<VvueNotificationResDto> vvueNotificationResDtoList;
    private Long lastCursorId;
    private boolean hasNext;
    @Builder
    public VvueNotificationListDto(List<VvueNotificationResDto> vvueNotificationResDtoList,
        Long lastCursorId, boolean hasNext) {
        this.vvueNotificationResDtoList = vvueNotificationResDtoList;
        this.lastCursorId = lastCursorId;
        this.hasNext = hasNext;
    }
}
