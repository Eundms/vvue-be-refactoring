package com.exciting.vvue.user;


import com.exciting.vvue.user.exception.UserNotFoundException;
import com.exciting.vvue.user.model.User;
import com.exciting.vvue.user.model.dto.UserDto;
import com.exciting.vvue.user.model.dto.UserModifyDto;

public interface UserService {

    UserDto getUserDto(Long userId) throws UserNotFoundException;

    void modifyUser(Long userId, UserModifyDto userModifyDto) throws UserNotFoundException;

    void delete(Long userId) throws UserNotFoundException;

    User getUserByEmailPassword(String email, String password);

    User getUserById(Long userId);

    User createUser(User user);
}

