package com.exciting.vvue.memory.model;

import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.user.model.User;
import jakarta.persistence.Entity;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "USERMEMORY", indexes = {
    @Index(name = "idx_user_schedule_memory", columnList = "user_id, schedule_memory_id"),
    @Index(name = "idx_schedule_memory", columnList = "schedule_memory_id")
})
@NamedEntityGraph(
    name = "UserMemory.withDetails",
    attributeNodes = {
        @NamedAttributeNode(value = "user", subgraph = "userWithPicture"),
        @NamedAttributeNode(value = "picture")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "userWithPicture",
            attributeNodes = @NamedAttributeNode("picture")
        )
    }
)
public class UserMemory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "schedule_memory_id", nullable = false)
  private ScheduleMemory scheduleMemory;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
  private String comment;

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "picture_id", nullable = false)
  private Picture picture;

  @Builder
  public UserMemory(Long id, ScheduleMemory scheduleMemory, User user, String comment,
      Picture picture) {
    this.id = id;
    this.scheduleMemory = scheduleMemory;
    this.user = user;
    this.comment = comment;
    this.picture = picture;
  }

  public static UserMemory with(String comment, Picture picture, ScheduleMemory scheduleMemory,
      User user) {
    return UserMemory.builder()
        .scheduleMemory(scheduleMemory)
        .user(user)
        .comment(comment)
        .picture(picture)
        .build();
  }
}
