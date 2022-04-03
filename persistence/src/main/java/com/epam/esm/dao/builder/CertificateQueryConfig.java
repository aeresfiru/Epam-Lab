package com.epam.esm.dao.builder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CertificateQueryConfig extends QueryConfig {
    private final String tagParam;
}
