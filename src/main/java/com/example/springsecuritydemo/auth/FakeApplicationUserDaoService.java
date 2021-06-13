package com.example.springsecuritydemo.auth;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.springsecuritydemo.security.UserRole.*;

@Repository("fake")
@RequiredArgsConstructor
public class FakeApplicationUserDaoService implements ApplicationUserDao {
    private final PasswordEncoder passwordEncoder;

    private List<ApplicationUser> getUsers() {
        return Lists.newArrayList(
                new ApplicationUser(
                        "ivan",
                        passwordEncoder.encode("password5834"),
                        new ArrayList<>(ADMIN.getGrantedAuthorities()),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "tom",
                        passwordEncoder.encode("password1"),
                        new ArrayList<>(ADMINTRAINEE.getGrantedAuthorities()),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "annasmith",
                        passwordEncoder.encode("password"),
                        new ArrayList<>(STUDENT.getGrantedAuthorities()),
                        true,
                        true,
                        true,
                        true
                )
        );
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getUsers()
                .stream()
                .filter(user -> Objects.equals(username, user.getUsername()))
                .findFirst();
    }
}
