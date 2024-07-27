package com.exciting.vvue.user.repository;

import com.exciting.vvue.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.provider = :provider and u.providerId = :providerId")
    User findByProviderAndProviderId(String provider, String providerId);
    @Query("select u from User u where u.nickname = :nickname")
    User findByNickname(String nickname);

}
