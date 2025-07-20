package com.exciting.vvue.user.service;

import com.exciting.vvue.user.model.User;
import java.util.Optional;

public interface UserRepository {

  User findByNickname(String nickname);

  Optional<User> findByProviderAndProviderId(String provider, String providerId);

  Optional<User> findById(Long id);

  User getReferenceById(long userId);

  User save(User prev);

  void delete(User user);

  boolean existsById(Long userId);
}
