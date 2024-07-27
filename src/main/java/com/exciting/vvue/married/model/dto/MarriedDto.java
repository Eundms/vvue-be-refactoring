package com.exciting.vvue.married.model.dto;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.picture.model.dto.PictureDto;
import com.exciting.vvue.user.model.dto.UserDto;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MarriedDto {

    @NotNull(message = "[필수] marriedId")
    private Long id;

    private String marriedDay;

    private PictureDto picture;

    private UserDto first;
    private UserDto second;

    @Builder
    public MarriedDto(Long id, String marriedDay, PictureDto picture, UserDto first,
        UserDto second) {
        this.id = id;
        this.marriedDay = marriedDay;
        this.picture = picture;
        this.first = first;
        this.second = second;
    }

    public static MarriedDto from(Married married) {
        return MarriedDto.builder()
            .id(married.getId())
            .marriedDay(married.getMarriedDay() == null ? null : married.getMarriedDay().toString())
            .picture(married.getPicture() == null ? null : PictureDto.from(married.getPicture()))
            .first(UserDto.from(married.getFirst()))
            .second(UserDto.from(married.getSecond()))
            .build();
    }
}
