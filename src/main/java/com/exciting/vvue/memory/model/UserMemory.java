package com.exciting.vvue.memory.model;

import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.user.model.User;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
