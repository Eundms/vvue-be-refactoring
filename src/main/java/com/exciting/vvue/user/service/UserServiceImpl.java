package com.exciting.vvue.user.service;

import com.exciting.vvue.auth.model.OAuthUserInfo;
import com.exciting.vvue.picture.exception.PictureNotFoundException;
import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.picture.repository.PictureRepository;
import com.exciting.vvue.user.UserService;
import com.exciting.vvue.user.dto.UserDto;
import com.exciting.vvue.user.dto.UserModifyDto;
import com.exciting.vvue.user.exception.UserNotFoundException;
import com.exciting.vvue.user.model.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PictureRepository pictureRepository;

  @Override
  public boolean existsById(Long userId) {
    return userRepository.existsById(userId);
  }

  @Override
  public UserDto getUserDto(Long userId) throws UserNotFoundException {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new UserNotFoundException("" + userId)
    );
    return UserDto.from(user);
  }

  @Override
  public Optional<User> getByProviderAndProviderId(String provider, String providerId) {
    return userRepository.findByProviderAndProviderId(provider, providerId);
  }

  @Override
  public User addOAuthUser(OAuthUserInfo oauthUser) {
    User newUser = User.builder()
        .email(oauthUser.getEmail())
        .provider(oauthUser.getProvider())
        .providerId(oauthUser.getProviderId())
        .nickname(oauthUser.getNickName())
        .build();
    return userRepository.save(newUser);
  }

  @Override
  public void modifyUser(Long userId, UserModifyDto user) throws UserNotFoundException {
    log.debug(
        "modifyUser " + userId + " " + user.getGender() + " " + user.getNickname() + " "
            + user.getBirthday());
    User prev = userRepository.findById(userId).orElseThrow(
        () -> new UserNotFoundException("" + userId));
    prev = modifyIfNotnull(prev, user);
    userRepository.save(prev);
  }

  private User modifyIfNotnull(User prev, UserModifyDto user) {
    if (user.getPictureId() != null) {
      Picture newProfile = pictureRepository.findById(user.getPictureId())
          .orElseThrow(() -> new PictureNotFoundException("" + user.getPictureId()));
      prev.setPicture(newProfile);
    }
    if (user.getGender() != null) {
      prev.setGender(user.getGender());
    }
    if (user.getBirthday() != null) {
      prev.setBirthday(user.getBirthday());
    }
    if (user.getNickname() != null) {
      prev.setNickname(user.getNickname());
    }
    return prev;
  }

  @Override
  public void delete(Long userId) throws UserNotFoundException {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new UserNotFoundException("" + userId));

    userRepository.delete(user);

  }

  @Override
  public User getUserById(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new UserNotFoundException("" + userId));
    userRepository.save(user);
    return user;
  }


}