package com.main.mainserver.model.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Set.of(Permission.ADMIN, Permission.PUBLISH, Permission.BASIC)),
    PUBLISHER(Set.of(Permission.PUBLISH, Permission.BASIC)),
    USER(Set.of(Permission.BASIC));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());

    }
}
