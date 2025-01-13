package com.exciting.vvue.married.service;

import org.springframework.stereotype.Service;

import com.exciting.vvue.married.MarriedService;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.dto.MarriedModifyDto;
import com.exciting.vvue.married.dto.req.MarriedCreateDto;
import com.exciting.vvue.picture.repository.PictureRepository;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.service.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MarriedServiceImpl implements MarriedService {
	private final MarriedRepository marriedRepository;
	private final UserRepository userRepository;
	private final PictureRepository pictureRepository;

	@Override
	public int getMarriedCount(Long id) {
		return marriedRepository.countByUserId(id);
	}

	@Override
	public Married getMarriedByUserIdWithDetails(Long id) {
		return marriedRepository.findByUserIdWithDetails(id);
	}

	@Override
	public Married getMarriedByUserid(Long userId) {
		return marriedRepository.findByUserId(userId);
	}

	@Override
	public void updateMarried(Long id, MarriedModifyDto marriedModifyDto) {
		Married married = marriedRepository.findByUserId(id);
		if (marriedModifyDto.getMarriedDay() != null)
			married.setMarriedDay(marriedModifyDto.getMarriedDay());

		if (marriedModifyDto.getPictureId() > 0) {
			married.setPicture(pictureRepository.findById(marriedModifyDto.getPictureId()).get());
		}

		marriedRepository.save(married);
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
	public Married deleteByUserId(Long userId) {
		Married married = marriedRepository.findByUserId(userId);
		if (married != null) {
			marriedRepository.delete(married);
		}
		return married;
	}

}
