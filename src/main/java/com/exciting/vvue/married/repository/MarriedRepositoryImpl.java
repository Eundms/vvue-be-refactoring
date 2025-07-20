package com.exciting.vvue.married.repository;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.repository.jpa.MarriedJpaRepository;
import com.exciting.vvue.married.service.MarriedRepository;
import com.exciting.vvue.picture.model.Picture;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
  public Long findMarriedIdByUserId(Long userId) {
    return marriedJpaRepository.findMarriedIdByUserId(userId);
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
  public Married deleteByUserId(Long userId) {
    marriedJpaRepository.deleteByUserId(userId);
    return null;
  }

  @Override
  public Optional<Married> findById(long marriedId) {
    return marriedJpaRepository.findById(marriedId);
  }

  @Override
  @Transactional
  public Long updateAndReturnId(Long userId, LocalDate marriedDay, Picture picture) {
    marriedJpaRepository.updateMarriedInfo(userId, marriedDay, picture);
    Long marriedId = findMarriedIdByUserId(userId);
    return marriedId;
  }

  @Override
  public Optional<Married> findByMarriedIdWithDetails(long marriedId) {
    return marriedJpaRepository.findByMarriedIdWithDetails(marriedId);
  }

  @Override
  public Long findSpouseIdByUserId(Long userId) {
    return marriedJpaRepository.findSpouseIdByUserId(userId);
  }
}
