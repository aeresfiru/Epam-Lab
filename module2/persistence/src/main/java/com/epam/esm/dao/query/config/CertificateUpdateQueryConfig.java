package com.epam.esm.dao.query.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CertificateUpdateQueryConfig extends UpdateQueryConfig {
    private final Long certificateId;
}
