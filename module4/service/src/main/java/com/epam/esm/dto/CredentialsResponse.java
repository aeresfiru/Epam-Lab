package com.epam.esm.dto;

import lombok.Data;

/**
 * CredentialsResponse
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Data
public class CredentialsResponse {

    private final String username;
    private final String token;
}
