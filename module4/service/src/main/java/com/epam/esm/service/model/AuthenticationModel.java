package com.epam.esm.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthenticationDto
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationModel {

    private String username;

    private String password;
}
