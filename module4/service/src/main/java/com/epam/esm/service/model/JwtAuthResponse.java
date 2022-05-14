package com.epam.esm.service.model;

import lombok.Data;

/**
 * CredentialsResponse
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Data
public class JwtAuthResponse {

    private final String username;
    private final String token;
}
