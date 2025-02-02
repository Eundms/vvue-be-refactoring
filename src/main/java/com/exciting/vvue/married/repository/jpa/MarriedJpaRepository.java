package com.exciting.vvue.married.repository.jpa;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.picture.model.Picture;

public interface MarriedJpaRepository extends JpaRepository<Married, Long> {

	@Query(value = "select count(married.id) from Married married where married.first.id=:id or married.second.id=:id")
	int countByUserId(Long id);

	@Query(value = "select married from Married married "
		+ "left join fetch married.first first "
		+ "left join fetch first.picture "
		+ "left join fetch married.second second "
		+ "left join fetch second.picture "
		+ "where married.first.id=:id or married.second.id=:id")
	Married findByUserIdWithDetails(Long id);

	@Query(value = "select married.id from Married married "
		+ "where married.first.id=:id or married.second.id=:id")
	Long findMarriedIdByUserId(Long id);

	boolean existsById(Long id);

	@Modifying
	@Query("UPDATE Married m SET m.marriedDay = :marriedDay, m.picture = :picture WHERE m.first.id = :id or m.second.id = :id")
	void updateMarriedInfo(Long id, LocalDate marriedDay, Picture picture);

	@Modifying
	@Query("DELETE FROM Married m WHERE m.first.id = :userId OR m.second.id = :userId")
	void deleteByUserId(Long userId);

	@Query(value = "select married from Married married "
		+ "left join fetch married.picture "
		+ "left join fetch married.first first "
		+ "left join fetch first.picture "
		+ "left join fetch married.second second "
		+ "left join fetch second.picture "
		+ "where married.id=:marriedId")
	Optional<Married> findByMarriedIdWithDetails(long marriedId);
}
