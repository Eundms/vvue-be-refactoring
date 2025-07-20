package com.exciting.vvue.memory.model;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.memory.dto.req.MemoryAddReqDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "SCHEDULEMEMORY", indexes = {
    @Index(name = "idx_married_id_id", columnList = "married_id, id"),
    @Index(name = "idx_schedule_id_married_id_schedule_date", columnList = "schedule_id, married_id, schedule_date"),
    @Index(name = "idx_schedule_date", columnList = "schedule_date")
})

public class ScheduleMemory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "schedule_id")
  private Long scheduleId;
  private String scheduleName;

  @Column(name = "schedule_date")
  private LocalDate scheduleDate;

  @OneToMany(mappedBy = "scheduleMemory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<UserMemory> userMemories;

  @OneToMany(mappedBy = "scheduleMemory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<PlaceMemory> placeMemories;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "married_id", nullable = false)
  private Married married;

  @CreatedDate
  private LocalDateTime createdAt;

  @Builder
  public ScheduleMemory(Long id, Long scheduleId, String scheduleName, LocalDate scheduleDate,
      Married married) {
    this.id = id;
    this.scheduleId = scheduleId;
    this.scheduleName = scheduleName;
    this.scheduleDate = scheduleDate;
    this.married = married;
  }

  public static ScheduleMemory with(MemoryAddReqDto memoryAddReqDto, Married married) {
    return ScheduleMemory.builder()
        .scheduleId(memoryAddReqDto.getScheduleId())
        .scheduleName(memoryAddReqDto.getScheduleName())
        .scheduleDate(LocalDate.parse(memoryAddReqDto.getScheduleDate()))
        .married(married)
        .build();
  }
}
