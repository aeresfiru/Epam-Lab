package com.epam.esm.service.impl;

import com.epam.esm.domain.Status;
import com.epam.esm.domain.User;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    private User user;

    @BeforeEach
    void setup() {
        user = new User(1L, "username", "password", Status.ACTIVE, new HashSet<>(), new HashSet<>());
    }

    @DisplayName("JUnit test for find User by id")
    @Test
    void givenUserId_whenFindById_thenReturnUser() {
        given(repository.findById(user.getId())).willReturn(Optional.of(user));

        User actual = service.findById(user.getId());

        assertThat(actual).isNotNull();
    }

    @DisplayName("JUnit test for find User by id (negative scenario)")
    @Test
    void givenUserId_whenFindById_thenThrowException() {
        given(repository.findById(user.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(user.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("JUnit test for find User by username")
    @Test
    void givenUserId_whenFindByUsername_thenReturnUser() {
        given(repository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        User actual = service.findByUsername(user.getUsername());

        assertThat(actual).isNotNull();
    }

    @DisplayName("JUnit test for find User by username (negative scenario)")
    @Test
    void givenUserId_whenFindByUsername_thenThrowException() {
        given(repository.findByUsername(user.getUsername())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByUsername(user.getUsername()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
