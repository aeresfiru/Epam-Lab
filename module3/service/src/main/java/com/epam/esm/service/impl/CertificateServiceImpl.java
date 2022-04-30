package com.epam.esm.service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Status;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.Mapper;
import com.epam.esm.dto.TagDto;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.builder.CertificatePageSettings;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.DuplicateEntityException;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.service.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CertificateServiceImpl
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Slf4j
@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;

    private final Mapper<Certificate, CertificateDto> certificateMapper;

    private final Mapper<Tag, TagDto> tagMapper;

    private final Validator<TagDto> tagValidator;

    private final Validator<Pagination> paginationValidator;

    private final Validator<CertificateDto> certificateValidator;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  Mapper<Certificate, CertificateDto> certificateMapper,
                                  Mapper<Tag, TagDto> tagMapper,
                                  Validator<TagDto> tagValidator,
                                  Validator<Pagination> paginationValidator,
                                  Validator<CertificateDto> certificateValidator) {

        this.certificateRepository = certificateRepository;
        this.certificateMapper = certificateMapper;
        this.tagMapper = tagMapper;
        this.tagValidator = tagValidator;
        this.paginationValidator = paginationValidator;
        this.certificateValidator = certificateValidator;
    }

    @Override
    public CertificateDto findById(long id) {
        log.info("IN findById, id: {}", id);
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND));
        return certificateMapper.toDto(certificate);
    }

    @Override
    public List<CertificateDto> findAll(CertificatePageSettings parameters, Pagination pagination) {
        log.info("IN findAll - params: {}, page: {}, pageSize: {}",
                parameters, pagination.getPage(), pagination.getPageSize());

        paginationValidator.validateForCreate(pagination);
        return certificateRepository.query(parameters, pagination).stream()
                .map(certificateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CertificateDto create(CertificateDto dto) {
        log.info("IN create - creating certificate: {}", dto);
        certificateValidator.validateForCreate(dto);
        this.checkForDuplicate(dto.getName());
        Certificate certificate = certificateMapper.toEntity(dto);
        return certificateMapper.toDto(certificateRepository.create(certificate));
    }

    @Override
    public CertificateDto update(CertificateDto dto, long id) {
        log.info("IN update - updating certificate, id: ({})", id);
        Optional<Certificate> certificateFromDb = certificateRepository.findById(id);
        if (!certificateFromDb.isPresent()) {
            throw new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND);
        }
        this.checkForDuplicate(dto.getName());

        certificateValidator.validateForUpdate(dto);
        dto.getTags().forEach(tagValidator::validateForCreate);

        Certificate toUpdate = certificateFromDb.get();
        updateCertificateFields(toUpdate, dto);
        return certificateMapper.toDto(certificateRepository.update(toUpdate));
    }

    @Override
    public void delete(long id) {
        log.info("IN delete - deleting certificate: {}", id);
        Optional<Certificate> optional = certificateRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND);
        }
        Certificate certificate = optional.get();
        certificate.setStatus(Status.DELETED);
        certificateRepository.update(certificate);
    }

    private void updateCertificateFields(Certificate certificateToUpdate, CertificateDto dto) {
        if (dto.getName() != null) {
            certificateToUpdate.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            certificateToUpdate.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            certificateToUpdate.setPrice(dto.getPrice());
        }
        if (dto.getDuration() != null) {
            certificateToUpdate.setDuration(dto.getDuration());
        }
        if (dto.getTags() != null) {
            certificateToUpdate.setTags(dto.getTags().stream().map(tagMapper::toEntity).collect(Collectors.toSet()));
        }
    }

    private void checkForDuplicate(String certificateName) {
        if (certificateName != null) {
            certificateRepository.findByName(certificateName).ifPresent(tag -> {
                throw new DuplicateEntityException(certificateName, ExceptionConstant.CERTIFICATE_DUPLICATE);
            });
        }
    }
}
