package com.epam.esm.service.dto.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.service.dto.DtoMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("certificateDtoMapper")
public class CertificateDtoMapper implements DtoMapper<Certificate, CertificateDto> {

    @Override
    public CertificateDto mapToDto(Certificate entity) {
        CertificateDto dto = new CertificateDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setDuration(entity.getDuration());
        dto.setPrice(entity.getPrice());
        dto.setTags(entity.getTags());
        return dto;
    }

    @Override
    public Certificate mapFromDto(CertificateDto dto) {
        return Certificate
                .builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .price(dto.getPrice())
                .tags(dto.getTags())
                .build();
    }
}
