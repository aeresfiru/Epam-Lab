package com.epam.esm.service;

import com.epam.esm.domain.User;

/**
 * AuthService
 *
 * @author alex
 * @version 1.0
 * @since 9.05.22
 */
public interface AuthService {

    User login(String login, String password);

    User register(User user);
}
