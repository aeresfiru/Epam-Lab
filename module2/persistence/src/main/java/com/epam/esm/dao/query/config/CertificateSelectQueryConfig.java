package com.epam.esm.dao.query.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CertificateSelectQueryConfig extends SelectQueryConfig {
    private final List<String> tagParam;
}
