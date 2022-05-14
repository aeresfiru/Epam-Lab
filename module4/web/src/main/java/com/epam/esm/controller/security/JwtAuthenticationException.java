package com.epam.esm.controller.security;

import org.springframework.security.core.AuthenticationException;

/**
 * JwtAuthenticationException
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
