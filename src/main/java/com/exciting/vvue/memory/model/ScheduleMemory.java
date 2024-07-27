package com.exciting.vvue.memory.model;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.memory.model.dto.req.MemoryAddReqDto;
import com.exciting.vvue.schedule.model.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "SCHEDULEMEMORY")
public class ScheduleMemory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long scheduleId;
    private String scheduleName;
    private LocalDate scheduleDate;

    @OneToMany(mappedBy = "scheduleMemory", cascade = CascadeType.ALL)
    private List<UserMemory> userMemories;

    @OneToMany(mappedBy = "scheduleMemory", cascade = CascadeType.ALL)
    private List<PlaceMemory> placeMemories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MARRIED_ID", nullable = false)
    private Married married;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public ScheduleMemory(Long id, Long scheduleId, String scheduleName, LocalDate scheduleDate, Married married) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.scheduleName = scheduleName;
        this.scheduleDate = scheduleDate;
        this.married = married;
    }

    public static ScheduleMemory with(MemoryAddReqDto memoryAddReqDto,  Married married) {
        return ScheduleMemory.builder()
            .scheduleId(memoryAddReqDto.getScheduleId())
            .scheduleName(memoryAddReqDto.getScheduleName())
            .scheduleDate(memoryAddReqDto.getScheduleDate())
            .married(married)
            .build();
    }
}
