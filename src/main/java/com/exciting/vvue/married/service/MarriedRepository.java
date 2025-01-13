package com.exciting.vvue.married.service;

import java.util.Optional;

import com.exciting.vvue.married.model.Married;

public interface MarriedRepository {

	int countByUserId(Long id);

	Married findByUserIdWithDetails(Long id);
	Married findByUserId(Long id);

	boolean existsById(Long id);

	Married save(Married married);

	void delete(Married married);

	Optional<Married> findById(long marriedId);

}
