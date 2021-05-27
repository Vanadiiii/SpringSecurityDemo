package com.example.springsecuritydemo.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.springsecuritydemo.security.UserPermission.*;

@AllArgsConstructor
@Getter
public enum UserRole {
    STUDENT(Set.of()),
    ADMIN(Set.of(COURSE_WRITE, COURSE_READ, STUDENT_READ, STUDENT_WRITE)),
    ADMINTRAINEE(Set.of(COURSE_READ, STUDENT_READ));

    private final Set<UserPermission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = this.permissions.stream()
                .map(UserPermission::getPermission)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
