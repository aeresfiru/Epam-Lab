package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.builder.SortingType;
import com.epam.esm.dao.builder.select.CertificateSelectQueryConfig;
import com.epam.esm.dao.builder.select.CertificateSortColumn;
import com.epam.esm.dao.builder.update.CertificateUpdateQueryConfig;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.CertificateConverter;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.dto.impl.CertificateDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDao certificateDao;
    private final DtoMapper<Certificate, CertificateDto> dtoMapper;
    private final TagDao tagDao;
    private final CertificateValidator certificateValidator;
    private final Validator<Tag> tagValidator;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao,
                                  DtoMapper<Certificate, CertificateDto> dtoMapper,
                                  TagDao tagDao,
                                  CertificateValidator certificateValidator,
                                  Validator<Tag> tagValidator) {
        this.certificateDao = certificateDao;
        this.dtoMapper = dtoMapper;
        this.tagDao = tagDao;
        this.certificateValidator = certificateValidator;
        this.tagValidator = tagValidator;
    }

    @Override
    @Transactional
    public CertificateDto createCertificate(CertificateDto dto) {
        if (certificateDao.readByName(dto.getName()).isPresent()) {
            throw new DuplicateEntityException(dto.getName(), ErrorCode.CERTIFICATE_ERROR);
        }
        Certificate entity = dtoMapper.mapFromDto(dto);
        certificateValidator.validate(entity);
        if (entity.getTags() != null) {
            entity.getTags().forEach(tagValidator::validate);
            entity.setTags(dto.getTags());
        }
        certificateDao.create(entity);
        this.linkTags(entity);
        return this.readById(entity.getId());
    }

    @Override
    public List<CertificateDto> readAllCertificates() {
        List<Certificate> certificates = certificateDao.readAll();
        certificates.forEach(c -> c.setTags(tagDao.readByCertificateId(c.getId())));
        return certificates.stream().map(dtoMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CertificateDto readById(Long id) {
        CertificateDto certificate = dtoMapper.mapToDto(certificateDao.readById(id)
                        .orElseThrow(() -> new EntityNotFoundException(id, ErrorCode.CERTIFICATE_ERROR)));
        certificate.setTags(tagDao.readByCertificateId(id));
        return certificate;
    }

    @Override
    @Transactional
    public CertificateDto updateCertificate(CertificateDto dto, Long id) {
        CertificateUpdateQueryConfig config = this.createCertificateConfigForUpdate(dto, id);
        Certificate entity = dtoMapper.mapFromDto(dto);
        entity.setId(id);
        certificateValidator.validateForUpdate(entity);
        entity.getTags().forEach(tagValidator::validate);
        certificateDao.update(config);
        this.unlinkTags(entity);
        this.linkTags(entity);
        return this.readById(id);
    }

    @Override
    @Transactional
    public void deleteCertificate(Long id) {
        if (!certificateDao.delete(id)) {
            throw new EntityNotFoundException(id, ErrorCode.CERTIFICATE_ERROR);
        }
    }

    @Override
    public List<CertificateDto> readCertificateByFilterQuery(Optional<String> searchQuery,
                                                             Optional<String> tagName,
                                                             Optional<String> sortParameters) {
        CertificateSelectQueryConfig config = CertificateSelectQueryConfig.builder()
                .searchQuery(searchQuery.orElse(null))
                .tagParam(tagName.orElse(null))
                .build();
        Map<String, SortingType> sortingMap = this.convertSortParametersIntoMap(sortParameters);
        if (!sortingMap.isEmpty()) {
            config.setParameterSortingTypeMap(sortingMap);
        }
        List<Certificate> certificates = certificateDao.query(config);
        certificates.forEach(c -> c.setTags(tagDao.readByCertificateId(c.getId())));
        return certificates.stream().map(dtoMapper::mapToDto).collect(Collectors.toList());
    }

    /**
     * The method associates a tag and a certificate in the database.
     * If such tag does not exist in the database, creates it.
     */
    private void linkTags(Certificate certificate) {
        if (certificate.getTags() != null) {
            certificate.getTags().stream()
                    .filter(tag -> !tagDao.readByName(tag.getName()).isPresent())
                    .forEach(tagDao::create);
            Set<Tag> tags = new HashSet<>();
            certificate.getTags().forEach(t -> tags.add(tagDao.readByName(t.getName())
                    .orElseThrow(() -> new EntityNotFoundException(t.getId(),
                            ErrorCode.TAG_ERROR))));
            tags.forEach(t ->
                    certificateDao.attachTagToCertificate(certificate.getId(), t.getId()));
        }
    }

    private void unlinkTags(Certificate certificate) {
        certificate.getTags()
                .stream().filter(t -> t.getId() != null)
                .forEach(t -> certificateDao.detachTagFromCertificate(certificate.getId(), t.getId()));
    }

    private CertificateUpdateQueryConfig createCertificateConfigForUpdate(CertificateDto certificate, Long id) {
        Map<String, Object> parameterMap = CertificateConverter.convertToParameterMap(certificate);
        return CertificateUpdateQueryConfig.builder()
                .certificateId(id)
                .paramsValueMap(parameterMap)
                .build();
    }

    /**
     * The method determines which sort types were passed by the user
     * and then turns them into a Map.
     */
    private Map<String, SortingType> convertSortParametersIntoMap(Optional<String> sortParameters) {
        Map<String, SortingType> sorting = new HashMap<>();
        if (sortParameters.isPresent()) {
            String[] elements = sortParameters.get().split(",");
            for (String element : elements) {
                SortingType direction = SortingType.ASC;
                if (element.startsWith("-")) {
                    element = element.substring(1);
                    direction = SortingType.DESC;
                }
                Optional<String> column = CertificateSortColumn.resolveDbName(element);
                if (column.isPresent()) {
                    sorting.put(column.get(), direction);
                }
            }
        }
        return sorting;
    }
}
