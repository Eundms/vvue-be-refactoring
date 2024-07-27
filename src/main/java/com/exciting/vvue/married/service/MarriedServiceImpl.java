package com.exciting.vvue.married.service;

import javax.transaction.Transactional;

import com.exciting.vvue.married.repository.MarriedRepository;
import org.springframework.stereotype.Service;

import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.married.model.dto.MarriedModifyDto;
import com.exciting.vvue.married.model.dto.req.MarriedCreateDto;
import com.exciting.vvue.picture.repository.PictureRepository;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MarriedServiceImpl implements MarriedService{
	private final MarriedRepository marriedRepository;
	private final UserRepository userRepository;
	private final PictureRepository pictureRepository;
	@Override
	public int getMarriedCount(Long id) {
		return marriedRepository.countByUserId(id);
	}

	@Override
	public Married getMarriedByUserId(Long id) {
		return marriedRepository.getMarriedByUserId(id);
	}

	@Override
	public Long getSpouseIdById(Long userId) {
		Married married = marriedRepository.getMarriedByUserId(userId);
		return married.getSpouseId(userId);
	}

	@Override
	public void updateMarried(Long id, MarriedModifyDto marriedModifyDto) {
		Married married = marriedRepository.getMarriedByUserId(id);
		if(marriedModifyDto.getMarriedDay() != null)
			married.setMarriedDay(marriedModifyDto.getMarriedDay());

		if(marriedModifyDto.getPictureId() > 0){
			married.setPicture(pictureRepository.findById(marriedModifyDto.getPictureId()).get());
		}

		marriedRepository.save(married);
	}

	@Override
	public boolean existById(Long id) {
		return marriedRepository.existsById(id);
	}


	@Override
	public void createMarried(Long id, MarriedCreateDto marriedCreateDto) {
		User me = userRepository.findById(id).get();
		User partner = userRepository.findById(marriedCreateDto.getPartnerId()).get();
		Married married = Married.builder()
			.marriedDay(marriedCreateDto.getMarriedDay())
			.picture(null)
			.first(me)
			.second(partner)
			.build();
		marriedRepository.save(married);
	}

	@Override
	public boolean existByUserId(Long id) {
		return marriedRepository.existsByFirst_IdOrSecond_Id(id, id);
	}

	@Override
	public int countByUserId(Long id) {
		return marriedRepository.countByUserId(id);
	}

	@Override
	public Married deleteByUserId(Long userId) {
		Married married = marriedRepository.getMarriedByUserId(userId);
		if (married != null) {
			marriedRepository.delete(married);
		}
		return married;
	}

}
