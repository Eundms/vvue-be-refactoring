package com.exciting.vvue.memory.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.exciting.vvue.picture.model.Picture;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "PLACEMEMORY_IMAGE")
public class PlaceMemoryImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLACEMEMORY_ID", nullable = false)
	private PlaceMemory placeMemory;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "PICTURE_ID", nullable = false)
	private Picture picture;

	@Builder
	public PlaceMemoryImage(Long id, PlaceMemory placeMemory, Picture picture) {
		this.id = id;
		this.placeMemory = placeMemory;
		this.picture = picture;
	}
}
