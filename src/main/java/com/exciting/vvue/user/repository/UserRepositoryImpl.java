package com.exciting.vvue.user.repository;

import java.util.Optional;

import com.exciting.vvue.user.service.UserRepository;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.repository.jpa.UserJpaRepository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private  final UserJpaRepository userJpaRepository;

    @Override
    public User findByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname);
    }

    @Override
    public User findByProviderAndProviderId(String provider, String providerId) {
        return userJpaRepository.findByProviderAndProviderId(provider, providerId);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public User getReferenceById(long userId) {
        return userJpaRepository.getReferenceById(userId);
    }

    @Override
    public User save(User prev) {
        return userJpaRepository.save(prev);
    }

    @Override
    public void delete(User user) {
        userJpaRepository.delete(user);
    }
}
