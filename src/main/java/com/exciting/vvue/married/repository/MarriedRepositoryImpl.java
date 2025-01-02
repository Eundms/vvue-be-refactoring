package com.exciting.vvue.married.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.married.service.MarriedRepository;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.repository.jpa.MarriedJpaRepository;

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
	public Married getMarriedByUserId(Long id) {
		return marriedJpaRepository.getMarriedByUserId(id);
	}

	@Override
	public boolean existsById(Long id) {
		return marriedJpaRepository.existsById(id);
	}

	@Override
	public boolean existsByFirst_IdOrSecond_Id(Long firstId, Long secondId) {
		return marriedJpaRepository.existsByFirst_IdOrSecond_Id(firstId, secondId);
	}

	@Override
	public void save(Married married) {
		marriedJpaRepository.save(married);
	}

	@Override
	public void delete(Married married) {
		marriedJpaRepository.delete(married);
	}

	@Override
	public Married getReferenceById(Long marriedId) {
		return marriedJpaRepository.getMarriedByUserId(marriedId);
	}

	@Override
	public Optional<Married> findById(long marriedId) {
		return marriedJpaRepository.findById(marriedId);
	}
}
