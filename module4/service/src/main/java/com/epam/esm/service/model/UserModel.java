package com.epam.esm.service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

/**
 * UserDto
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserModel extends RepresentationModel<UserModel> {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
}
