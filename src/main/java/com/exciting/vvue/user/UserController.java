package com.exciting.vvue.user;

import com.exciting.vvue.auth.AuthContext;
import com.exciting.vvue.user.dto.UserDto;
import com.exciting.vvue.user.dto.UserModifyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;

  @GetMapping
  @Override
  public UserDto getUserInfoByToken() {
    Long userId = AuthContext.getUserId();
    log.debug("유저 정보 조회" + userId);
    return userService.getUserDto(userId);
  }

  @PutMapping
  @Override
  public void modify(@RequestBody UserModifyDto userModifyDto) {
    Long userId = AuthContext.getUserId();
    userService.modifyUser(userId, userModifyDto);
  }

  @DeleteMapping
  @Override
  public void delete() {
    Long userId = AuthContext.getUserId();
    userService.delete(userId);
  }
}
