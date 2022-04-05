package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.builder.update.CertificateUpdateQueryConfig;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.exception.EntityNotFoundException;
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

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao,
                                  DtoMapper<Certificate, CertificateDto> dtoMapper,
                                  TagDao tagDao) {
        this.certificateDao = certificateDao;
        this.dtoMapper = dtoMapper;
        this.tagDao = tagDao;
    }

    @Override
    @Transactional
    public CertificateDto createCertificate(CertificateDto certificateDto) {
        Certificate saved = dtoMapper.mapFromDto(certificateDto);
        if (certificateDao.create(saved)) {
            this.linkTags(certificateDto, saved.getId());
            return this.readById(saved.getId());
        } else {
            throw new RuntimeException("Can't create certificate");
        }
    }

    private void linkTags(CertificateDto dto, Long certificateId) {
        if (dto.getTags() != null) {
            dto.getTags().stream()
                    .filter(tag -> !tagDao.readByName(tag.getName()).isPresent())
                    .forEach(tagDao::create);
            Set<Tag> tags = new HashSet<>();
            dto.getTags().forEach(t -> tags.add(tagDao.readByName(t.getName()).orElseThrow(EntityNotFoundException::new)));
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
                        .orElseThrow(EntityNotFoundException::new));
        certificate.setTags(tagDao.readByCertificateId(id));
        return certificate;
    }

    @Override
    @Transactional
    public CertificateDto updateCertificate(CertificateDto certificateDto, Long id) {
        CertificateUpdateQueryConfig config = this.createCertificateConfig(certificateDto, id);
        if (certificateDao.update(config)) {
            this.unlinkTags(this.readById(id));
            this.linkTags(certificateDto, certificateDto.getId());
            return this.readById(certificateDto.getId());
        } else {
            throw new RuntimeException();
        }
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
            throw new RuntimeException();
        }
    }

    @Override
    public List<Certificate> readCertificateByFilterQuery(Optional<String> searchQuery,
                                                          Optional<String> tagName) {
        return null;
    }
}
