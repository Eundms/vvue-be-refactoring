package com.exciting.vvue.picture.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE picture SET is_deleted = true WHERE id = ?")
@NoArgsConstructor
@Entity
public class Picture {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String url; //TODO : url /images/** 만 저장하도록 변경
  @Enumerated(EnumType.STRING)
  private AccessLevel accessLevel;
  private boolean isDeleted;

  @Builder
  public Picture(Long id, String url, AccessLevel accessLevel, boolean isDeleted) {
    this.id = id;
    this.url = url;
    this.accessLevel = accessLevel;
    this.isDeleted = isDeleted;
  }

}
