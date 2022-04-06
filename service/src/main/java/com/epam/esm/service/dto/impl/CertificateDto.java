package com.epam.esm.service.dto.impl;

import com.epam.esm.domain.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
public class CertificateDto {

    Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Short duration;

    private Set<Tag> tags;
}
