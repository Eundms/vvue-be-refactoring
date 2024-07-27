package com.exciting.vvue.memory.model.dto.res;

import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.memory.model.ScheduleMemory;
import com.exciting.vvue.memory.model.UserMemory;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class MemoryResDto {
    private Long id; // ScheduleMemory.id

   private ScheduleResDto scheduleInfo;

    private List<UserMemoryResDto> userMemories;
    private List<PlaceMemoryResDto> placeMemories; // 장소 id별로 그룹핑해서 보내야함

    @Builder
    public MemoryResDto(Long id, ScheduleResDto scheduleInfo, List<UserMemoryResDto> userMemories,
        List<PlaceMemoryResDto> placeMemories) {
        this.id = id;
        this.scheduleInfo = scheduleInfo;
        this.userMemories = userMemories;
        this.placeMemories = placeMemories;
    }

    public static MemoryResDto from(ScheduleMemory scheduleMemory) {
        List<PlaceMemory> placeMemories = scheduleMemory.getPlaceMemories();
        List<UserMemory> userMemories = scheduleMemory.getUserMemories();
        return MemoryResDto.builder()
            .id(scheduleMemory.getId())
            .scheduleInfo(ScheduleResDto.builder()
                .id(scheduleMemory.getScheduleId())
                .name(scheduleMemory.getScheduleName())
                .date(scheduleMemory.getScheduleDate())
                .build())
            .userMemories(userMemories.stream().map(UserMemoryResDto::from).toList())
            .placeMemories(PlaceMemoryResDto.from(placeMemories))
            .build();
    }
}
