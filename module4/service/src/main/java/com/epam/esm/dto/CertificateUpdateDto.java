package com.epam.esm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * CertificateUpdateDto
 *
 * @author alex
 * @version 1.0
 * @since 9.05.22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CertificateUpdateDto extends RepresentationModel<CertificateUpdateDto> {

    @Size(min = 2, max = 50)
    private String name;

    @Size(min = 3, max = 250)
    private String description;

    @DecimalMin(value = "0.01")
    private BigDecimal price;

    @Positive
    private Integer duration;

    @Valid
    private Set<TagDto> tags = new HashSet<>();
}
