package com.exciting.vvue.place.model;

import com.exciting.vvue.user.model.User;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FavoritePlace {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Place place;

  @Builder
  public FavoritePlace(User user, Place place) {
    this.user = user;
    this.place = place;
  }
}
