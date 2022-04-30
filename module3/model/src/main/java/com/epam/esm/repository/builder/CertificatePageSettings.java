package com.epam.esm.repository.builder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CertificatePageSettings extends PageSettings {
    private List<String> tagParam;
}
