package com.epam.esm.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CreateOrderDto {

    private Long userId;

    private Set<CertificateDto> certificates;
}
