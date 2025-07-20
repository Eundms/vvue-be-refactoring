package com.exciting.vvue.picture.model;

public enum PictureUsedFor {
  USER_PROFILE("user", AccessLevel.PUBLIC),
  MARRIED_RELATED("married", AccessLevel.MARRIED);
  private final String name;
  private final AccessLevel accessLevel;

  PictureUsedFor(String name, AccessLevel accessLevel) {
    this.name = name;
    this.accessLevel = accessLevel;
  }

  public String getName() {
    return this.name;
  }

  public AccessLevel getAccessLevel() {
    return this.accessLevel;
  }
}
