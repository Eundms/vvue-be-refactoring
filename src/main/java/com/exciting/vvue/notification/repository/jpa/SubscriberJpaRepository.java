package com.exciting.vvue.notification.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exciting.vvue.notification.model.Subscriber;

@Repository
public interface SubscriberJpaRepository extends JpaRepository<Subscriber, Long> { // Redis로 바꾸기!
	@Query("select sb from Subscriber sb where sb.userId=:userId")
	Optional<Subscriber> findByUserId(Long userId);

	@Query("select sb from Subscriber sb where sb.userId=:userId and sb.firebaseToken=:firebaseToken")
	Optional<Subscriber> findByUserIdAndFirebaseToken(Long userId, String firebaseToken);


	@Query("SELECT s FROM Subscriber s WHERE s.userId IN :receiverIds")
	List<Subscriber> findAllByUserId(List<Long> receiverIds);

	@Modifying
	@Query("UPDATE Subscriber s SET s.firebaseToken = :firebaseToken WHERE s.userId = :userId")
	void update(Long userId, String firebaseToken);

	@Modifying
	@Query(value = "INSERT INTO subscriber (user_id, firebase_token) VALUES (:userId, :firebaseToken) " +
		"ON DUPLICATE KEY UPDATE firebase_token = :firebaseToken", nativeQuery = true)
	void saveOrUpdate(@Param("userId") Long userId, @Param("firebaseToken") String firebaseToken);

}
