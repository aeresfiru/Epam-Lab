package com.epam.esm.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TagDto
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TagModel extends RepresentationModel<TagModel> {

    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;
}
