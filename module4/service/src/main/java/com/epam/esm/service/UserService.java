package com.epam.esm.service;

import com.epam.esm.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * UserService
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
public interface UserService {

    User findById(Long id);

    Page<User> findAll(Pageable pageable);

    User findByUsername(String username);
}
