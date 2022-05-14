package com.epam.esm.controller.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * JwtUser
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@AllArgsConstructor
@Getter
public class JwtUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String email;
    private final boolean enabled;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final Date lastPasswordResetDate;
    private final Collection<? extends GrantedAuthority> authorities;
}
