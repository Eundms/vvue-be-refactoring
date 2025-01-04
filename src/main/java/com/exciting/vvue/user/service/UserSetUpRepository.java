package com.exciting.vvue.user.service;

import com.exciting.vvue.user.model.dto.UserRelatedInfo;

public interface UserSetUpRepository {
	UserRelatedInfo getAllRelatedInfo(Long userId);

}
