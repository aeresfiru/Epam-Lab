package com.epam.esm.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * OrderCreateDto
 *
 * @author alex
 * @version 1.0
 * @since 9.05.22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderCreateModel extends RepresentationModel<OrderCreateModel> {

    @NotNull(message = "Order should have customer id")
    private Long customerId;

    @NotEmpty(message = "Order should have certificates")
    private Set<Long> certificateIds;
}
