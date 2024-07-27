package com.exciting.vvue.memory.model;

import com.exciting.vvue.memory.model.dto.req.PlaceMemoryReqDto;
import com.exciting.vvue.place.model.Place;
import com.exciting.vvue.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="PLACEMEMORY")
public class PlaceMemory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ScheduleMemory scheduleMemory;

    @OneToOne
    private User user;

    @ManyToOne
    private Place place; //PLACE_ID

    private Float rating;
    private String comment;

    @OneToMany(mappedBy = "placeMemory", cascade = CascadeType.ALL, orphanRemoval = true)
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
        this.rating = rating;
        this.comment = comment;
        this.placeMemoryImageList = placeMemoryImageList;
    }

    public static PlaceMemory with(PlaceMemoryReqDto placeMemoryReqDto, ScheduleMemory scheduleMemory, Place place,User user) {
        return PlaceMemory.builder()
            .scheduleMemory(scheduleMemory)
            .user(user)
            .place(place)
            .rating(placeMemoryReqDto.getRating())
            .comment(placeMemoryReqDto.getComment())
            .build();
    }
}
