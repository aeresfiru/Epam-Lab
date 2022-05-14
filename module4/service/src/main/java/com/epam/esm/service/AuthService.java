package com.epam.esm.service;

import com.epam.esm.domain.User;
import com.epam.esm.service.model.AuthenticationModel;

/**
 * AuthService
 *
 * @author alex
 * @version 1.0
 * @since 14.05.22
 */
public interface AuthService {

    User login(AuthenticationModel authenticationModel);

    void signup(User user);
}
