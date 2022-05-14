package com.epam.esm.controller.security.jwt;

import com.epam.esm.domain.Role;
import com.epam.esm.domain.Status;
import com.epam.esm.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JwtUserFactory
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getEmail(),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getLastUpdateDate(),
                mapGrantedAuthorities(user.getRoles())
        );
    }

    private static Collection<GrantedAuthority> mapGrantedAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toSet());
    }
}
