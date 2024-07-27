package com.exciting.vvue.memory.model.dto.res;

import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.picture.model.dto.PictureDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlaceCommentResDto {

    private Long id;
    private Float rating;
    private String comment;

    private CommentedUserDto user;


    @Builder
    public PlaceCommentResDto(Long id, Float rating, String comment, CommentedUserDto user) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
    }

    public static PlaceCommentResDto from(PlaceMemory placeMemory) {
        return PlaceCommentResDto.builder()
            .id(placeMemory.getId())
            .rating(placeMemory.getRating())
            .comment(placeMemory.getComment())
            .user(CommentedUserDto.from(placeMemory.getUser()))
            .build();
    }
}
