package com.epam.esm.dto;

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
public class OrderCreateDto extends RepresentationModel<OrderCreateDto> {

    @NotEmpty
    private Set<Certificate> certificates;
}
