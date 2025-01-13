package com.exciting.vvue.married.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.repository.jpa.MarriedJpaRepository;
import com.exciting.vvue.married.service.MarriedRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MarriedRepositoryImpl implements MarriedRepository {
	private final MarriedJpaRepository marriedJpaRepository;

	@Override
	public int countByUserId(Long id) {
		return marriedJpaRepository.countByUserId(id);
	}

	@Override
	public Married findByUserIdWithDetails(Long userId) {
		return marriedJpaRepository.findByUserIdWithDetails(userId);
	}

	@Override
	public Married findByUserId(Long userId) {
		return marriedJpaRepository.findByUserId(userId);
	}

	@Override
	public boolean existsById(Long id) {
		return marriedJpaRepository.existsById(id);
	}


	@Override
	public Married save(Married married) {
		return marriedJpaRepository.save(married);
	}

	@Override
	public void delete(Married married) {
		marriedJpaRepository.delete(married);
	}

	@Override
	public Optional<Married> findById(long marriedId) {
		return marriedJpaRepository.findById(marriedId);
	}

}
