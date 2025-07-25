package com.exciting.vvue.married.service;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.picture.model.Picture;
import java.time.LocalDate;
import java.util.Optional;

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

  Long findSpouseIdByUserId(Long userId);
}
