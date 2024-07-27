package com.exciting.vvue.user.model.dto;

import com.exciting.vvue.picture.model.dto.PictureDto;
import com.exciting.vvue.user.model.User;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String nickname;
    private String email;
    private PictureDto picture;

    @Builder
    public UserDto(Long id, String nickname, String email, PictureDto picture) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;
    }

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail()==null?null:user.getEmail())
                .picture(user.getPicture()==null?null:PictureDto.from(user.getPicture()))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
