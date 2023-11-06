package com.main.mainserver.security;

import com.main.mainserver.exception.UserIsNotFoundException;
import com.main.mainserver.model.user.User;
import com.main.mainserver.repository.UserJPARepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJPARepository userJPARepository;

    public UserDetailsServiceImpl(UserJPARepository userJPARepository) {
        this.userJPARepository = userJPARepository;
    }

    // TODO на сколько ок что я ищу по емейлу, а не name? https://www.baeldung.com/spring-security-extra-login-fields если будет время
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userJPARepository.findUserByEmail(email).orElseThrow(() -> new UserIsNotFoundException(email));
        return SecurityUser.fromUser(user);
    }
}
