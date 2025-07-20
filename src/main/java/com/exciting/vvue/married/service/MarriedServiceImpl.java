package com.exciting.vvue.married.service;

import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.married.dto.MarriedModifyDto;
import com.exciting.vvue.married.dto.req.MarriedCreateDto;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.picture.repository.PictureRepository;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.service.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MarriedServiceImpl implements MarriedService {

  private final MarriedRepository marriedRepository;
  private final UserRepository userRepository;
  private final PictureRepository pictureRepository;

  @Override
  public Married getMarriedByUserIdWithDetails(Long id) {
    return marriedRepository.findByUserIdWithDetails(id);
  }

  @Override
  public Long getMarriedIdByUserId(Long userId) {
    return marriedRepository.findMarriedIdByUserId(userId);
  }

  @Override
  @Transactional
  public Long updateMarriedAndReturnId(Long userId, MarriedModifyDto marriedModifyDto) {
    Picture picture = null;

    if (marriedModifyDto.getPictureId() > 0) {
      picture = pictureRepository.findById(marriedModifyDto.getPictureId()).orElse(null);
    }

    return marriedRepository.updateAndReturnId(userId, marriedModifyDto.getMarriedDay(), picture);
  }

  @Override
  public Long createMarried(Long id, MarriedCreateDto marriedCreateDto) {
    User me = userRepository.findById(id).get();
    User partner = userRepository.findById(marriedCreateDto.getPartnerId()).get();
    Married married = Married.builder()
        .marriedDay(marriedCreateDto.getMarriedDay())
        .picture(null)
        .first(me)
        .second(partner)
        .build();
    Married created = marriedRepository.save(married);
    return created.getId();
  }

  @Override
  public int countByUserId(Long id) {
    return marriedRepository.countByUserId(id);
  }

  @Override
  public void deleteByUserId(Long userId) {
    marriedRepository.deleteByUserId(userId);
  }

  @Override
  public Long getSpouseId(Long userId) {
    return marriedRepository.findSpouseIdByUserId(userId);
  }

}
