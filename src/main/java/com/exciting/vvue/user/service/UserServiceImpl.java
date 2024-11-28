package com.exciting.vvue.user.service;

import org.springframework.stereotype.Service;

import com.exciting.vvue.picture.exception.PictureNotFoundException;
import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.picture.repository.PictureRepository;
import com.exciting.vvue.user.UserService;
import com.exciting.vvue.user.exception.UserNotFoundException;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.model.dto.UserDto;
import com.exciting.vvue.user.model.dto.UserModifyDto;
import com.exciting.vvue.user.repository.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryImpl userRepository;
    private final PictureRepository pictureRepository;
    @Override
    public UserDto getUserDto(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("" + userId)
        );
        return UserDto.from(user);
    }

    @Override
    public void modifyUser(Long userId, UserModifyDto user) throws UserNotFoundException {
        log.debug("modifyUser " + userId + " " +user.getGender()+" "+ user.getNickname() + " " + user.getBirthday());
        User prev = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("" + userId));
        prev = modifyIfNotnull(prev, user);
        userRepository.save(prev);
    }

    private User modifyIfNotnull(User prev, UserModifyDto user) {
        if (user.getPictureId() != null){
            Picture newProfile = pictureRepository.findById(user.getPictureId()).orElseThrow(()-> new PictureNotFoundException(""+user.getPictureId()));
            prev.setPicture(newProfile);
        }
        if(user.getGender()!=null)
            prev.setGender(user.getGender());
        if (user.getBirthday() != null)
            prev.setBirthday(user.getBirthday());
        if (user.getNickname() != null)
            prev.setNickname(user.getNickname());
        prev = checkIsAuthenticated(prev);
        return prev;
    }

    @Override
    public void delete(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("" + userId));

        userRepository.delete(user);

    }

	@Override
	public User getUserByEmailPassword(String email, String password) {
		User user = userRepository.findByEmailAndPassword(email, password).orElseThrow(
			() -> new UserNotFoundException("" + email));

		return null;
	}

	@Override
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new UserNotFoundException("" + userId));
        user = checkIsAuthenticated(user);
        userRepository.save(user);
        return user;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }


    private User checkIsAuthenticated(User user){
        if (user.getGender() != null && user.getBirthday() != null && user.getNickname() != null) {
            user.setAuthenticated(true);
        }else{
            user.setAuthenticated(false);
        }
        return user;
    }
}
