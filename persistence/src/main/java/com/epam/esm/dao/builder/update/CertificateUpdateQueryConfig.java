package com.epam.esm.dao.builder.update;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CertificateUpdateQueryConfig extends UpdateQueryConfig{

    private final Long certificateId;
}
