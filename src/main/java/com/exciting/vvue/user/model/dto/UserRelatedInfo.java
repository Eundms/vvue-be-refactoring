package com.exciting.vvue.user.model.dto;

import lombok.Getter;

@Getter
public class UserRelatedInfo {
	private boolean isAuthenticated; // user.isAuthenticated
	private boolean isSpouseConnected; // married != null
	private boolean isSpouseInfoAdded; // married != null && married.getMarriedDay() != null

	public UserRelatedInfo(boolean isAuthenticated, boolean isSpouseConnected, boolean isSpouseInfoAdded) {
		this.isAuthenticated = isAuthenticated;
		this.isSpouseConnected = isSpouseConnected;
		this.isSpouseInfoAdded = isSpouseInfoAdded;
	}
}
