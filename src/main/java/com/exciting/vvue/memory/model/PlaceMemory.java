package com.exciting.vvue.memory.model;

import com.exciting.vvue.memory.dto.req.PlaceMemoryReqDto;
import com.exciting.vvue.place.model.Place;
import com.exciting.vvue.user.model.User;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "PLACEMEMORY", indexes = {
    @Index(name = "idx_place_id", columnList = "place_id")
})
@NamedEntityGraph(
    name = "PlaceMemory.withDetails",
    attributeNodes = {
        @NamedAttributeNode(value = "place"),
        @NamedAttributeNode(value = "placeMemoryImageList", subgraph = "imageDetails")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "imageDetails",
            attributeNodes = @NamedAttributeNode("picture")
        )
    }
)
public class PlaceMemory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "schedule_memory_id", nullable = false)
  private ScheduleMemory scheduleMemory;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "place_id", nullable = false)
  private Place place; //PLACE_ID

  private Float rating;
  private String comment;

  @BatchSize(size = 10) // 한 번에 가져올 크기
  @OneToMany(mappedBy = "placeMemory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<PlaceMemoryImage> placeMemoryImageList;//PlaceBlockImage

  @CreatedDate
  private LocalDateTime createdAt;

  @Builder
  public PlaceMemory(Long id, ScheduleMemory scheduleMemory, User user, Place place,
      Float rating,
      String comment, List<PlaceMemoryImage> placeMemoryImageList) {
    this.id = id;
    this.scheduleMemory = scheduleMemory;
    this.user = user;
    this.place = place;
    this.rating = Math.min(Math.max(rating, 0.0f), 5.0f);
    this.comment = comment;
    this.placeMemoryImageList = placeMemoryImageList;
  }

  public static PlaceMemory with(PlaceMemoryReqDto placeMemoryReqDto, ScheduleMemory scheduleMemory,
      Place place,
      User user) {
    return PlaceMemory.builder()
        .scheduleMemory(scheduleMemory)
        .user(user)
        .place(place)
        .rating(placeMemoryReqDto.getRating())
        .comment(placeMemoryReqDto.getComment())
        .build();
  }
}
