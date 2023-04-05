package com.m19y.learn.role;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public enum AppUserPermission {

  // authorities
  STUDENT_READ("student:read"),
  STUDENT_WRITE("student:write"),
  COURSE_READ("course:read"),
  COURSE_WRITE("course:write");


  private final String permission;

  AppUserPermission(String permission) {
    this.permission = permission;
  }

  public String getPermission() {
    return permission;
  }


}
