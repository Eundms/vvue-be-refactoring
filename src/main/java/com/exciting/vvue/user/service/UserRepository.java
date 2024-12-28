package com.exciting.vvue.user.service;

import java.util.Optional;

import com.exciting.vvue.user.model.User;

public interface UserRepository {
	User findByNickname(String nickname);

	User findByProviderAndProviderId(String provider, String providerId);
	Optional<User> findById(Long id);

	User getReferenceById(long userId);

	User save(User prev);

	void delete(User user);
}
