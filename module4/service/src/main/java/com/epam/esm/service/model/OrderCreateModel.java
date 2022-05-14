package com.epam.esm.model;

import com.epam.esm.domain.Certificate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * OrderCreateDto
 *
 * @author alex
 * @version 1.0
 * @since 9.05.22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderCreateModel extends RepresentationModel<OrderCreateModel> {

    @NotEmpty
    private Set<Certificate> certificates;
}
