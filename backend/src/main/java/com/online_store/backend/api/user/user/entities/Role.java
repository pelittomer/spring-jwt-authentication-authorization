package com.online_store.backend.api.user.user.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    ADMIN,
    SELLER,
    CUSTOMER;

    Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }
}
