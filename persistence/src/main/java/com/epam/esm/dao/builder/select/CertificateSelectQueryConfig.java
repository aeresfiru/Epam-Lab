package com.epam.esm.dao.builder.select;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CertificateSelectQueryConfig extends SelectQueryConfig {
    private final String tagParam;
}
