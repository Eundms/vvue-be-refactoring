package com.exciting.vvue.married.service;

import java.util.Optional;

import com.exciting.vvue.married.model.Married;
public interface MarriedRepository {

	int countByUserId(Long id);

	Married getMarriedByUserId(Long id);

	boolean existsById(Long id);

	boolean existsByFirst_IdOrSecond_Id(Long firstId, Long secondId);

	void save(Married married);

	void delete(Married married);

	Married getReferenceById(Long marriedId);

	Optional<Married> findById(long marriedId);
}
