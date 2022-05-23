package com.epam.esm.controller.security;

import com.epam.esm.controller.security.jwt.JwtUser;
import com.epam.esm.controller.security.jwt.JwtUserFactory;
import com.epam.esm.domain.User;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailService
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Service
@Slf4j
@AllArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user == null) throw new UsernameNotFoundException("User not found, username: " + username);

        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("IN loadUserByUsername - user with username {}, successfully loaded", username);
        return jwtUser;
    }
}
