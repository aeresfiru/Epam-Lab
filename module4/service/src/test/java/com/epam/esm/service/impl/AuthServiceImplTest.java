package com.epam.esm.service.impl;

import com.epam.esm.domain.Status;
import com.epam.esm.domain.User;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.model.AuthenticationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    private AuthenticationModel authenticationModel;

    @BeforeEach
    void setup() {
        user = new User(1L, "username", "password", Status.ACTIVE, new HashSet<>(), new HashSet<>());
        authenticationModel = new AuthenticationModel(user.getUsername(), user.getPassword());
    }

    @DisplayName("JUnit test for sign up method")
    @Test
    void givenUserModel_whenSignUp_thenNothing() {
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.empty());
        given(userRepository.save(user)).willReturn(user);

        authService.signup(user);

        verify(userRepository, times(1)).save(user);
    }

    @DisplayName("JUnit test for login method")
    @Test
    void givenAuthenticationModel_whenLogin_thenReturnUser() {
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        User savedUser = authService.login(authenticationModel);
        assertThat(savedUser).isNotNull();
    }
}