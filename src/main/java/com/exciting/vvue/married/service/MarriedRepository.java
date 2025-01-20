package com.exciting.vvue.married.service;

import java.time.LocalDate;
import java.util.Optional;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.picture.model.Picture;

public interface MarriedRepository {

	int countByUserId(Long id);

	Married findByUserIdWithDetails(Long id);
	Long findMarriedIdByUserId(Long id);

	boolean existsById(Long id);

	Married save(Married married);

	Married deleteByUserId(Long userId);

	Optional<Married> findById(long marriedId);

	Long updateAndReturnId(Long userId, LocalDate marriedDay, Picture picture);

	Optional<Married> findByMarriedIdWithDetails(long marriedId);
}
