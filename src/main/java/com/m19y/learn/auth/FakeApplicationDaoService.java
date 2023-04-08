package com.m19y.learn.auth;

import com.google.common.collect.Lists;
import com.m19y.learn.role.AppUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("fake")
public class FakeApplicationDaoService implements  ApplicationUserDao{

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public FakeApplicationDaoService(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
    return getApplicationUsers()
            .stream()
            .filter(applicationUser -> username.equals(applicationUser.getUsername()))
            .findFirst();
  }

  private List<ApplicationUser> getApplicationUsers(){
    List<ApplicationUser> list = Lists.newArrayList(
            new ApplicationUser(
                    AppUserRole.STUDENT.getGrantedAuthorities(),
                    "pelajar",
                    passwordEncoder.encode("pass"),
                    true,
                    true,
                    true,
                    true
            ),
            new ApplicationUser(
                    AppUserRole.ADMIN.getGrantedAuthorities(),
                    "staf",
                    passwordEncoder.encode("pass"),
                    true,
                    true,
                    true,
                    true
            ),
            new ApplicationUser(
                    AppUserRole.ADMINTRAINEE.getGrantedAuthorities(),
                    "guru",
                    passwordEncoder.encode("pass"),
                    true,
                    true,
                    true,
                    true
            )

    );
    return list;
  }
}
