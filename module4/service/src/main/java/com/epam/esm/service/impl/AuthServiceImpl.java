package com.epam.esm.service.impl;

import com.epam.esm.domain.Role;
import com.epam.esm.domain.Status;
import com.epam.esm.domain.User;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.AuthService;
import com.epam.esm.service.DuplicateEntityException;
import com.epam.esm.service.model.AuthenticationModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * AuthServiceImpl
 *
 * @author alex
 * @version 1.0
 * @since 14.05.22
 */
@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(AuthenticationModel authenticationModel) throws UsernameNotFoundException {
        log.info("IN login - login user with username {}", authenticationModel.getUsername());
        String username = authenticationModel.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username,
                authenticationModel.getPassword())
        );
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username:" + username + ", not found");
        }
        return user;
    }

    @Override
    public void signup(User user) {
        if (userRepository.exists(Example.of(user))) {
            throw new DuplicateEntityException(user.getUsername(), "User already exists");
        }
        setUserFields(user);
        userRepository.save(user);
        log.info("IN signup - user with username: {} created successfully", user.getUsername());
    }

    private void setUserFields(User user) {
        Role role = roleRepository.findByName("ROLE_USER");
        user.setStatus(Status.ACTIVE);
        user.setRoles(Collections.singleton(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
