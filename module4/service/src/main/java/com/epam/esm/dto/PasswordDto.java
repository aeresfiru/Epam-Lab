package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PasswordDto
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Data
public class PasswordDto {

    private String email;

    private String oldPassword;

    private String newPassword;
}

