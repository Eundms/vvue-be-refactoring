package com.exciting.vvue.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString@Getter
@AllArgsConstructor
public class UserAuthenticated {
    private boolean isAuthenticated;
}
