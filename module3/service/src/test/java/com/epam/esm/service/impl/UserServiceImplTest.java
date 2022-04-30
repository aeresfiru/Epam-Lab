package com.epam.esm.service.impl;

import com.epam.esm.CertificatesApplication;
import com.epam.esm.domain.User;
import com.epam.esm.dto.Mapper;
import com.epam.esm.dto.UserDto;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.IncorrectParameterException;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import com.epam.esm.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {UserRepositoryImpl.class, CertificatesApplication.class})
@ActiveProfiles("test")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    private Mapper<User, UserDto> mapper;

    private UserService userService;

    @Autowired
    private Validator<Pagination> paginationValidator;

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(userRepository, mapper, paginationValidator);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void whenFindAll_thenReturnLimitedListOfTagDto(int pageSize) {
        Pagination pagination = new Pagination(1, pageSize);

        List<User> expected = new LinkedList<>();
        for (int i = 0; i < pageSize; i++) {
            expected.add(new User());
        }
        when(userRepository.findAll(pagination)).thenReturn(expected);

        List<UserDto> actual = userService.findAll(pagination);
        assertEquals(pageSize, actual.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4})
    void whenFindAll_thenThrowIncorrectParameterExceptionForPageSize(int pageSize) {
        Pagination pagination = new Pagination(1, pageSize);
        assertThrows(IncorrectParameterException.class,
                () -> userService.findAll(pagination));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4})
    void whenFindAll_thenThrowIncorrectParameterExceptionForPageNumber(int page) {
        Pagination pagination = new Pagination(page, 3);
        assertThrows(IncorrectParameterException.class,
                () -> userService.findAll(pagination));
    }

    @Test
    void whenFindById_thenReturnTagDto() {
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));

        UserDto actual = userService.findById(id);
        assertNotNull(actual);
    }

    @Test
    void whenFindById_thenThrowEntityNotFoundException() {
        long id = -1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(id));
    }
}