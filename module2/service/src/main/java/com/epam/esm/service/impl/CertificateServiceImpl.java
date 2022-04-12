package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.query.SortingType;
import com.epam.esm.dao.query.config.CertificateSelectQueryConfig;
import com.epam.esm.dao.query.config.CertificateUpdateQueryConfig;
import com.epam.esm.dao.query.impl.CertificateColumn;
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
import com.epam.esm.service.validator.TagValidator;
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
    private final CertificateConverter certificateConverter;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao,
                                  DtoMapper<Certificate, CertificateDto> dtoMapper,
                                  TagDao tagDao,
                                  CertificateConverter certificateConverter) {
        this.certificateDao = certificateDao;
        this.dtoMapper = dtoMapper;
        this.tagDao = tagDao;
        this.certificateConverter = certificateConverter;
    }

    @Override
    @Transactional
    public CertificateDto createCertificate(CertificateDto dto) {
        if (certificateDao.readByName(dto.getName()).isPresent()) {
            throw new DuplicateEntityException(dto.getName(), ErrorCode.CERTIFICATE_ERROR);
        }
        CertificateValidator.validate(dto);
        Certificate entity = dtoMapper.mapFromDto(dto);
        certificateDao.create(entity);
        if (dto.getTags() != null) {
            dto.getTags().forEach(TagValidator::validate);
            entity.setTags(dto.getTags());
            this.linkTags(dto, entity.getId());
        }
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
        Optional<Certificate> nameDuplicate = certificateDao.readByName(dto.getName());
        if (nameDuplicate.isPresent() && !nameDuplicate.get().getId().equals(id)) {
            throw new DuplicateEntityException(dto.getName(), ErrorCode.CERTIFICATE_ERROR);
        }
        CertificateValidator.validateForUpdate(dto);
        CertificateUpdateQueryConfig config = this.createCertificateConfigForUpdate(dto, id);
        certificateDao.update(config);

        if (dto.getTags() != null) {
            dto.getTags().forEach(TagValidator::validate);
            this.unlinkTags(this.readById(id));
            this.linkTags(dto, id);
        }
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
                                                             Optional<List<String>> tagNames,
                                                             Optional<String> sortParameters) {
        CertificateSelectQueryConfig config =
                this.createCertificateConfigForSelect(searchQuery, tagNames, sortParameters);
        List<Certificate> certificates = certificateDao.query(config);
        certificates.forEach(c -> c.setTags(tagDao.readByCertificateId(c.getId())));
        return certificates.stream().map(dtoMapper::mapToDto).collect(Collectors.toList());
    }

    /**
     * The method associates a tag and a certificate in the database.
     * If such tag does not exist in the database, creates it.
     */
    private void linkTags(CertificateDto dto, Long id) {
        //create witch does not exist
        dto.getTags().stream()
                .filter(tag -> !tagDao.readByName(tag.getName()).isPresent())
                .forEach(tagDao::create);
       for (Tag tag : dto.getTags()) {
           if (tag.getId() == null) {
               Optional<Tag> tagFromDb = tagDao.readByName(tag.getName());
               if (tagFromDb.isPresent()) {
                   tag = tagFromDb.get();
               }
           }
           certificateDao.attachTagToCertificate(id, tag.getId());
       }
    }

    private void unlinkTags(CertificateDto dto) {
        dto.getTags()
                .stream().filter(t -> t.getId() != null)
                .forEach(t -> certificateDao.detachTagFromCertificate(dto.getId(), t.getId()));
    }

    private CertificateSelectQueryConfig createCertificateConfigForSelect(Optional<String> searchQuery,
                                                                          Optional<List<String>> tagName,
                                                                          Optional<String> sortParameters) {
        CertificateSelectQueryConfig config = CertificateSelectQueryConfig.builder()
                .searchQuery(searchQuery.orElse(null))
                .tagParam(tagName.orElse(null))
                .build();
        if (sortParameters.isPresent()) {
            Map<String, SortingType> sortingMap = this.convertSortParametersIntoMap(sortParameters);
            config.setParameterSortingTypeMap(sortingMap);
        }
        return config;
    }

    private CertificateUpdateQueryConfig createCertificateConfigForUpdate(CertificateDto certificate, Long id) {
        Map<String, Object> parameterMap = certificateConverter.convertToParameterMap(certificate);
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
                Optional<String> column = CertificateColumn.resolveDbName(element);
                if (column.isPresent()) {
                    sorting.put(column.get(), direction);
                }
            }
        }
        return sorting;
    }
}
