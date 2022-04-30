package com.epam.esm.repository.impl;

import com.epam.esm.domain.User;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
class UserRepositoryImplTest {

    private static final User USER_1 = new User(1L);
    private static final User USER_2 = new User(2L);
    private static final User USER_3 = new User(3L);

    private final UserRepository userRepository;

    @Autowired
    UserRepositoryImplTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void whenFindAll_thenReturnAllUsers() {
        List<User> expected = Arrays.asList(USER_1, USER_2, USER_3);
        List<User> actual = userRepository.findAll(new Pagination(1,3));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenFindById_thenReturnUserWithThatId() {
        Optional<User> expected = Optional.of(USER_1);
        Optional<User> actual = userRepository.findById(USER_1.getId());

        Assertions.assertEquals(expected, actual);
    }
}