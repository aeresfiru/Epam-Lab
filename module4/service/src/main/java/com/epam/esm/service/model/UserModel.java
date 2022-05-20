package com.epam.esm.service.model;

import com.epam.esm.service.validator.OnPersist;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

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

    @NotNull(groups = OnPersist.class)
    @Length(min = 3, max = 95, groups = OnPersist.class)
    @Length(min = 3)
    private String username;

    @NotNull(groups = OnPersist.class)
    @Length(min = 3, max = 95, groups = OnPersist.class)
    @Length(min = 3)
    private String firstName;

    @NotNull(groups = OnPersist.class)
    @Length(min = 3, max = 95, groups = OnPersist.class)
    @Length(min = 3)
    private String lastName;

    @NotNull(groups = OnPersist.class)
    @Email
    private String email;

    @NotNull(groups = OnPersist.class)
    @Length(min = 7, max = 60, groups = OnPersist.class)
    @Length(min = 7)
    private String password;
}
