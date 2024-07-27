package com.exciting.vvue.memory.model.dto.res;

import com.exciting.vvue.picture.model.dto.PictureDto;
import com.exciting.vvue.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommentedUserDto {

    private Long id;
    private String nickname;
    private PictureDto picture;

    @Builder
    public CommentedUserDto(Long id, String nickname, PictureDto picture) {
        this.id = id;
        this.nickname = nickname;
        this.picture = picture;
    }

    public static CommentedUserDto from(User user) {
        return CommentedUserDto.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .picture(user.getPicture() == null ? null : PictureDto.from(user.getPicture()))
            .build();
    }

}
