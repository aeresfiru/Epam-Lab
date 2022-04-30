package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.repository.Pagination;

import java.util.List;

/**
 * UserService
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
public interface UserService {

    UserDto findById(long id);

    List<UserDto> findAll(Pagination pagination);
}
