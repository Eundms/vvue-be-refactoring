package com.exciting.vvue.place.service;

import org.springframework.stereotype.Service;

import com.exciting.vvue.place.FavoritePlaceService;
import com.exciting.vvue.place.exception.FavoritePlaceNotFoundException;
import com.exciting.vvue.place.exception.PlaceNotFoundException;
import com.exciting.vvue.place.model.FavoritePlace;
import com.exciting.vvue.place.model.Place;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.repository.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FavoritePlaceServiceImpl implements FavoritePlaceService {
	private final FavoritePlaceRepository favoritePlaceRepository;
	private final PlaceRepository placeRepository;
	private final UserRepositoryImpl userRepository;

	@Override
	public boolean checkScrap(long userId, long placeId) {
		return favoritePlaceRepository.findByUser_IdAndPlaceId(userId, placeId).isPresent();
	}

	@Override
	public void addScrap(long userId, long placeId) {
		User user = userRepository.getReferenceById(userId);
		Place place = placeRepository.getReferenceById(placeId);
		FavoritePlace favoritePlace = FavoritePlace.builder().user(user).place(place).build();

		favoritePlaceRepository.save(favoritePlace);
	}

	@Override
	public void deleteScrap(long userId, long placeId) {
		FavoritePlace favoritePlace = favoritePlaceRepository.findByUser_IdAndPlaceId(userId, placeId).orElseThrow(
			() -> new FavoritePlaceNotFoundException("" + placeId)
		);

		favoritePlaceRepository.delete(favoritePlace);
	}
}
