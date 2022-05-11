package com.epam.esm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * CertificateCreateDto
 *
 * @author alex
 * @version 1.0
 * @since 9.05.22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CertificateCreateDto extends RepresentationModel<CertificateCreateDto> {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @Size(min = 3, max = 250)
    private String description;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal price;

    @NotNull
    @Positive
    private Integer duration;

    @Valid
    private Set<TagDto> tags = new HashSet<>();
}
