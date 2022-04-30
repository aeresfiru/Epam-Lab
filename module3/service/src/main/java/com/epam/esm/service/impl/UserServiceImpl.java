package com.epam.esm.service.impl;

import com.epam.esm.domain.User;
import com.epam.esm.dto.Mapper;
import com.epam.esm.dto.UserDto;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import com.epam.esm.service.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserServiceImpl
 *
 * @author alex
 * @version 1.0
 * @since 25.04.22
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Mapper<User, UserDto> userDtoMapper;

    private final Validator<Pagination> paginationValidator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           Mapper<User, UserDto> userDtoMapper,
                           Validator<Pagination> paginationValidator) {

        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.paginationValidator = paginationValidator;
    }

    @Override
    public UserDto findById(long id) {
        log.info("IN findById - userId: {}", id);
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND));
        return userDtoMapper.toDto(user);
    }

    @Override
    public List<UserDto> findAll(Pagination pagination) {
        log.info("IN findAll - page: {}, pageSize: {}", pagination.getPage(), pagination.getPageSize());
        this.paginationValidator.validateForCreate(pagination);
        return userRepository.findAll(pagination).stream()
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
