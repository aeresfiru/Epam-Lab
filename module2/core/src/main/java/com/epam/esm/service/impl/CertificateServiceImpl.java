package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.TagValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.service.exception.ErrorCode.CERTIFICATE_NOT_FOUND;
import static com.epam.esm.service.exception.ErrorCode.DUPLICATE_CERTIFICATE_NAME;

@Service
@AllArgsConstructor
@Slf4j
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDao certificateDao;

    private final DtoMapper<Certificate, CertificateDto> certificateDtoMapper;

    private final TagDao tagDao;

    @Override
    @Transactional
    public CertificateDto createCertificate(CertificateDto dto) {
        log.info("Create certificate method called. {}", dto);
        this.validateForCreate(dto);
        Certificate certificate = certificateDtoMapper.mapFromDto(dto);
        certificateDao.create(certificate);
        this.linkTags(certificate);
        return readById(certificate.getId());
    }

    @Override
    public List<CertificateDto> readAllCertificates() {
        log.info("Read all certificates method called.");
        return certificateDao.readAll().stream()
                .peek(c -> c.setTags(tagDao.readByCertificateId(c.getId())))
                .map(certificateDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CertificateDto readById(Long id) {
        log.info("Read certificate by id method called. Id = {}", id);
        return certificateDtoMapper.mapToDto(this.readByIdAndSetTags(id));
    }

    @Override
    @Transactional
    public CertificateDto updateCertificate(CertificateDto dto, Long id) {
        log.info("Update certificate method called. {}", dto);
        this.validateForUpdate(dto, id);

        Certificate fromRequest = certificateDtoMapper.mapFromDto(dto);
        Certificate existing = this.readByIdAndSetTags(id);

        this.updateCertificateFields(existing, fromRequest);
        certificateDao.update(existing);
        this.updateTags(existing, fromRequest);
        return this.readById(id);
    }

    @Override
    @Transactional
    public void deleteCertificate(Long id) {
        log.info("Delete certificate by id method called. Id = {}", id);
        certificateDao.delete(id);
    }

    private Certificate readByIdAndSetTags(long id) {
        Certificate certificate = certificateDao.readById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, CERTIFICATE_NOT_FOUND));
        certificate.setTags(tagDao.readByCertificateId(id));
        return certificate;
    }

    private void validateForCreate(CertificateDto dto) {
        CertificateValidator.validate(dto);
        if (certificateDao.readByName(dto.getName()).isPresent()) {
            throw new DuplicateEntityException(dto.getName(), DUPLICATE_CERTIFICATE_NAME);
        }
    }

    private void validateForUpdate(CertificateDto dto, long id) {
        CertificateValidator.validateForUpdate(dto);
        Optional<Certificate> existing = certificateDao.readByName(dto.getName());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new DuplicateEntityException(dto.getName(), DUPLICATE_CERTIFICATE_NAME);
        }
    }

    private void updateTags(Certificate fromDb, Certificate fromRequest) {
        if (fromRequest.getTags() != null) {
            this.unlinkTags(fromDb, fromRequest);
            this.linkTags(fromDb, fromRequest);
        }
    }

    private void linkTags(Certificate fromRequest) {
        fromRequest.getTags().stream()
                .peek(this::createTagIfNotExist)
                .forEach(tag -> certificateDao.attachTagToCertificate(fromRequest.getId(), tag.getId()));
    }

    private void linkTags(Certificate fromDb, Certificate fromRequest) {
        fromRequest.getTags().stream()
                .filter(tag -> !fromDb.getTags().contains(tag))
                .peek(this::createTagIfNotExist)
                .peek(fromDb::addTag)
                .forEach(tag -> certificateDao.attachTagToCertificate(fromDb.getId(), tag.getId()));
    }

    private void createTagIfNotExist(Tag tag) {
        if (tag.getId() == null) {
            TagValidator.validate(tag);
            tagDao.create(tag);
        }
    }

    private void unlinkTags(Certificate fromDb, Certificate fromRequest) {
        fromDb.getTags().stream()
                .filter(tag -> !fromRequest.getTags().contains(tag))
                .forEach(tag -> certificateDao.detachTagFromCertificate(fromDb.getId(), tag.getId()));
    }

    private void updateCertificateFields(Certificate fromDb, Certificate certificate) {
        if (Objects.nonNull(certificate.getName())) {
            fromDb.setName(certificate.getName());
        }
        if (Objects.nonNull(certificate.getDescription())) {
            fromDb.setDescription(certificate.getDescription());
        }
        if (Objects.nonNull(certificate.getPrice())) {
            fromDb.setPrice(certificate.getPrice());
        }
        if (Objects.nonNull(certificate.getDuration())) {
            fromDb.setDuration(certificate.getDuration());
        }
    }
}
