package com.epam.esm.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

/**
 * UserDto
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserModel extends RepresentationModel<UserModel> {

    private Long id;

    private String username;

    @JsonIgnore
    private String password;
}
