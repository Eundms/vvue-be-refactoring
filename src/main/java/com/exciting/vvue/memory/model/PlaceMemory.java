package com.exciting.vvue.memory.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.exciting.vvue.memory.dto.req.PlaceMemoryReqDto;
import com.exciting.vvue.place.model.Place;
import com.exciting.vvue.user.model.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "PLACEMEMORY")
@NamedEntityGraph(
	name = "PlaceMemory.withDetails",
	attributeNodes = {
		@NamedAttributeNode(value="place"),
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
		this.rating = rating;
		this.comment = comment;
		this.placeMemoryImageList = placeMemoryImageList;
	}

	public static PlaceMemory with(PlaceMemoryReqDto placeMemoryReqDto, ScheduleMemory scheduleMemory, Place place,
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
