package com.exciting.vvue.user.repository;

import java.util.Optional;

import com.exciting.vvue.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.nickname = :nickname")
    User findByNickname(String nickname);

    @Query("select u from User u where u.email = :email and u.password = :password")
    Optional<User> findByEmailAndPassword(String email, String password);

}
