package com.exciting.vvue.auth.service;

import com.exciting.vvue.auth.model.OAuthUserInfo;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserRegistrationService {

  private final UserRepository userRepository;

  public User registerIfNotExists(OAuthUserInfo oauthUser) {
    return userRepository.findByProviderAndProviderId(oauthUser.getProvider(),
            oauthUser.getProviderId())
        .orElseGet(() -> {
          User newUser = User.builder()
              .email(oauthUser.getEmail())
              .provider(oauthUser.getProvider())
              .providerId(oauthUser.getProviderId())
              .nickname(oauthUser.getNickName())
              .build();
          return userRepository.save(newUser);
        });
  }
}
