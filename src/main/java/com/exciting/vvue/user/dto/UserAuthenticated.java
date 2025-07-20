package com.exciting.vvue.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class UserAuthenticated {

  private boolean isAuthenticated;
}
