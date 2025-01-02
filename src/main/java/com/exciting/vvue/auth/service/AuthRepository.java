package com.exciting.vvue.auth.service;

import com.exciting.vvue.auth.model.Auth;

public interface AuthRepository {

	void save(Auth auth);

	Auth findById(Long userId);
}
