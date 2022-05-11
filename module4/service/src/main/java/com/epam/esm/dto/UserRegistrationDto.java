package com.epam.esm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

/**
 * UserRegistrationDto
 *
 * @author alex
 * @version 1.0
 * @since 9.05.22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRegistrationDto extends RepresentationModel<UserRegistrationDto> {

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    @Length(max = 60)
    private String password;

    private String matchingPassword;
}
