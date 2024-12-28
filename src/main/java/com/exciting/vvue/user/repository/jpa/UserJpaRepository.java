package com.exciting.vvue.user.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.exciting.vvue.user.model.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.nickname = :nickname")
    User findByNickname(String nickname);


    @Query("select u from User u where u.provider = :provider and u.providerId = :providerId")
    User findByProviderAndProviderId(String provider, String providerId);

}
