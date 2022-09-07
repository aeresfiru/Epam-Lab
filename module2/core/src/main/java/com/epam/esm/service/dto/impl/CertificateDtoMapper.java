package com.epam.esm.service.dto.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.dto.TagDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CertificateDtoMapper implements DtoMapper<Certificate, CertificateDto> {

    private final DtoMapper<Tag, TagDto> tagDtoMapper;

    @Override
    public CertificateDto mapToDto(Certificate certificate) {
        CertificateDto dto = CertificateDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .duration(certificate.getDuration())
                .price(certificate.getPrice())
                .updated(certificate.getUpdated())
                .created(certificate.getCreated())
                .build();
        if (!CollectionUtils.isEmpty(certificate.getTags())) {
            dto.setTags(certificate.getTags().stream()
                    .map(tagDtoMapper::mapToDto)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    @Override
    public Certificate mapFromDto(CertificateDto dto) {
        Certificate certificate = Certificate.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .price(dto.getPrice())
                .updated(dto.getUpdated())
                .created(dto.getCreated())
                .build();
        if (!CollectionUtils.isEmpty(dto.getTags())) {
            certificate.setTags(dto.getTags().stream()
                    .map(tagDtoMapper::mapFromDto)
                    .collect(Collectors.toSet()));
        }
        return certificate;
    }
}
