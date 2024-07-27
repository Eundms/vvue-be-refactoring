package com.exciting.vvue.user.model.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserInfoUpdated {
    private boolean isAuthenticated;
    private boolean isSpouseConnected;
    private boolean isSpouseInfoAdded;
    public UserInfoUpdated(boolean isAuthenticated, boolean isSpouseConnected, boolean isSpouseInfoAdded) {
        this.isAuthenticated = isAuthenticated;
        this.isSpouseConnected = isSpouseConnected;
        this.isSpouseInfoAdded = isSpouseInfoAdded;
    }
}
