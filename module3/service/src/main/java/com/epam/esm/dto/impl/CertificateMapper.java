package com.epam.esm.dto.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * CertificateDtoMapper
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Component
@Qualifier("certificateDtoMapper")
public class CertificateMapper extends Mapper<Certificate, CertificateDto> {

    protected CertificateMapper(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public Certificate toEntity(CertificateDto dto) {
        return modelMapper.map(dto, Certificate.class);
    }

    @Override
    public CertificateDto toDto(Certificate certificate) {
        return modelMapper.map(certificate, CertificateDto.class);
    }
}
