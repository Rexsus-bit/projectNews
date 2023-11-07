package com.main.mainserver.security;

import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserStatus;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final Boolean isActive;
    private final Long id;

    public SecurityUser(String username, String password, List<SimpleGrantedAuthority> authorities, Boolean isActive, Long id) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUser (User user){
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(), user.getPassword(),
//                user.getUserStatus().equals(UserStatus.ACTIVE),
//                user.getUserStatus().equals(UserStatus.ACTIVE),
//                user.getUserStatus().equals(UserStatus.ACTIVE),
//                user.getUserStatus().equals(UserStatus.ACTIVE),
//                user.getRole().getAuthorities()
//        );

    return new SecurityUser(user.getEmail(), user.getPassword(), user.getRole().getAuthorities().stream().toList(),
            user.getUserStatus().equals(UserStatus.ACTIVE), user.getId());
    }
}
