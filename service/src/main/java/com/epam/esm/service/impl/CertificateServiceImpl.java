package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.builder.SortingType;
import com.epam.esm.dao.builder.select.CertificateSelectQueryConfig;
import com.epam.esm.dao.builder.select.CertificateSortColumn;
import com.epam.esm.dao.builder.update.CertificateUpdateQueryConfig;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.dto.impl.CertificateDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public CertificateDto createCertificate(CertificateDto certificateDto) {
        if (certificateDao.readByName(certificateDto.getName()).isPresent()) {
            throw new DuplicateEntityException(certificateDto.getName(), ErrorCode.CERTIFICATE_ERROR);
        }
        Certificate entity = dtoMapper.mapFromDto(certificateDto);
        certificateValidator.validate(entity);
        if (entity.getTags() != null) {
            entity.getTags().forEach(tagValidator::validate);
        }
        certificateDao.create(entity);
        this.linkTags(certificateDto, entity.getId());
        return this.readById(entity.getId());
    }

    private void linkTags(CertificateDto dto, Long certificateId) {
        if (dto.getTags() != null) {
            dto.getTags().stream()
                    .filter(tag -> !tagDao.readByName(tag.getName()).isPresent())
                    .forEach(tagDao::create);
            Set<Tag> tags = new HashSet<>();
            dto.getTags().forEach(t -> tags.add(tagDao.readByName(t.getName())
                    .orElseThrow(() -> new EntityNotFoundException(t.getId(),
                            ErrorCode.TAG_ERROR))));
            tags.forEach(t ->
                    certificateDao.attachTagToCertificate(certificateId, t.getId()));
        }
    }

    @Override
    public List<CertificateDto> readAllCertificates() {
        List<Certificate> certificates = certificateDao.readAll();
        certificates.forEach(c -> c.setTags(tagDao.readByCertificateId(c.getId())));
        return certificates.stream().map(dtoMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CertificateDto readById(Long id) {
        CertificateDto certificate = dtoMapper.mapToDto(
                certificateDao.readById(id)
                        .orElseThrow(() -> new EntityNotFoundException(id,
                                ErrorCode.CERTIFICATE_ERROR)));
        certificate.setTags(tagDao.readByCertificateId(id));
        return certificate;
    }

    @Override
    @Transactional
    public CertificateDto updateCertificate(CertificateDto certificateDto, Long id) {
        CertificateUpdateQueryConfig config = this.createCertificateConfig(certificateDto, id);
        Certificate entity = dtoMapper.mapFromDto(certificateDto);
        certificateValidator.validateForUpdate(entity);
        entity.getTags().forEach(tagValidator::validate);
        certificateDao.update(config);
        this.unlinkTags(this.readById(id));
        this.linkTags(certificateDto, id);
        return this.readById(id);
    }

    private void unlinkTags(CertificateDto certificate) {
        certificate.getTags()
                .stream().filter(t -> t.getId() != null)
                .forEach(t -> certificateDao.detachTagFromCertificate(certificate.getId(), t.getId()));
    }

    private CertificateUpdateQueryConfig createCertificateConfig(CertificateDto certificate, Long id) {
        Map<String, Object> parameterMap = convertCertificateToParameterMap(certificate);
        return CertificateUpdateQueryConfig.builder()
                .certificateId(id)
                .paramsValueMap(parameterMap)
                .build();
    }

    private static Map<String, Object> convertCertificateToParameterMap(CertificateDto certificate) {
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(certificate, Map.class);
        map = map.entrySet().stream().filter(k ->
                k.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return map;
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
                                                             Optional<List<String>> sorts) {
        CertificateSelectQueryConfig config = CertificateSelectQueryConfig.builder()
                .searchQuery(searchQuery.orElse(null))
                .tagParam(tagName.orElse(null))
                .build();
        this.addSortsToConfig(config, sorts);
        List<Certificate> certificates = certificateDao.query(config);
        certificates.forEach(c -> c.setTags(tagDao.readByCertificateId(c.getId())));
        return certificates.stream().map(dtoMapper::mapToDto).collect(Collectors.toList());
    }

    private void addSortsToConfig(CertificateSelectQueryConfig config,
                                  Optional<List<String>> sorts) {
        if (sorts.isPresent()) {
            Map<String, SortingType> sorting = new HashMap<>();
            sorts.get().forEach(s -> {
                String[] elements = s.split(":");
                Optional<String> column = CertificateSortColumn.resolveDbName(elements[0]);
                if (column.isPresent()) {
                    SortingType direction = SortingType.valueOf(elements[1].toUpperCase());
                    sorting.put(column.get(), direction);
                }
            });
            config.setParameterSortingTypeMap(sorting);
        }
    }
}
