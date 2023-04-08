package com.m19y.learn.auth;

import java.util.Optional;

public interface ApplicationUserDao {

  Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
