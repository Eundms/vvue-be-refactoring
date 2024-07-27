package com.exciting.vvue.married.repository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exciting.vvue.married.model.Married;
@Repository
public interface MarriedRepository extends JpaRepository<Married, Long> {

	@Query(value = "select count(married.id) from Married married where married.first.id=:id or married.second.id=:id")
	int countByUserId(Long id);

	@Query(value = "select married from Married married where married.first.id=:id or  married.second.id=:id")
	Married getMarriedByUserId(Long id);

	// married id 값으로 객체 찾기
	boolean existsById(Long id);

	boolean existsByFirst_IdOrSecond_Id(Long firstId, Long secondId);
}
