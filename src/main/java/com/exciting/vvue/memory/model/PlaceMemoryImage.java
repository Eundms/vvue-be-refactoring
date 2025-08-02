package com.exciting.vvue.memory.model;

import com.exciting.vvue.picture.model.Picture;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "PLACEMEMORY_IMAGE", indexes = {
    @Index(name = "idx_place_memory", columnList = "placememory_id")
})
@BatchSize(size = 5)
public class PlaceMemoryImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "placememory_id", nullable = false)
  private PlaceMemory placeMemory;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "picture_id", nullable = false)
  private Picture picture;

  @Builder
  public PlaceMemoryImage(Long id, PlaceMemory placeMemory, Picture picture) {
    this.id = id;
    this.placeMemory = placeMemory;
    this.picture = picture;
  }
}
