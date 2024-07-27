package com.exciting.vvue.picture.model;

import com.exciting.vvue.picture.model.dto.PictureDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String url;
    private boolean isDeleted;

    @Builder
    public Picture(Long id, String url, boolean isDeleted) {
        this.id = id;
        this.url = url;
        this.isDeleted = isDeleted;
    }

    public static Picture from(PictureDto pictureDto) {
        return Picture.builder()
                .id(pictureDto.getId())
                .url(pictureDto.getUrl())
                .build();
    }
}
