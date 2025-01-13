package com.exciting.vvue.married.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.exciting.vvue.married.model.Married;

public interface MarriedJpaRepository extends JpaRepository<Married, Long> {

	@Query(value = "select count(married.id) from Married married where married.first.id=:id or married.second.id=:id")
	int countByUserId(Long id);

	@Query(value = "select married from Married married "
		+ "left join fetch married.picture "
		+ "left join fetch married.first first "
		+ "left join fetch first.picture "
		+ "left join fetch married.second second "
		+ "left join fetch second.picture "
		+ "where married.first.id=:id or married.second.id=:id")
	Married findByUserIdWithDetails(Long id);

	@Query(value = "select married from Married married "
		+ "where married.first.id=:id or married.second.id=:id")
	Married findByUserId(Long id);

	boolean existsById(Long id);

}
