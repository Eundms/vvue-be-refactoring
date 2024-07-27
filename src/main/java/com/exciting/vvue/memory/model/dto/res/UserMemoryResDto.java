package com.exciting.vvue.memory.model.dto.res;

import com.exciting.vvue.memory.model.UserMemory;
import com.exciting.vvue.picture.model.dto.PictureDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserMemoryResDto {

    private Long id;
    private String comment;
    private PictureDto picture;
    private CommentedUserDto user;

    @Builder
    public UserMemoryResDto(Long id, String comment, PictureDto picture, CommentedUserDto user) {
        this.id = id;
        this.comment = comment;
        this.picture = picture;
        this.user = user;
    }

    public static UserMemoryResDto from(UserMemory userMemory) {
        return UserMemoryResDto.builder()
            .id(userMemory.getId())
            .comment(userMemory.getComment())
            .picture(PictureDto.from(userMemory.getPicture()))
            .user(CommentedUserDto.from(userMemory.getUser()))
            .build();
    }

}
