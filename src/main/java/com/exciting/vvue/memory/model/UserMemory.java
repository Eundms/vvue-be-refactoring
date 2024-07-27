package com.exciting.vvue.memory.model;

import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.user.model.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "USERMEMORY")
public class UserMemory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private ScheduleMemory scheduleMemory;
    @OneToOne
    private User user;
    private String comment;

    @OneToOne(orphanRemoval = true)
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
