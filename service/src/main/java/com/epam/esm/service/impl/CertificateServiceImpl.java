package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.builder.update.CertificateUpdateQueryConfig;
import com.epam.esm.domain.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDao certificateDao;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }

    @Override
    public Certificate createCertificate(Certificate certificate) {
        if (certificateDao.create(certificate)) {
            return certificate;
        } else {
            throw new RuntimeException("Can't create certificate");
        }
    }

    @Override
    public List<Certificate> readAllCertificates() {
        return certificateDao.readAll();
    }

    @Override
    public Certificate readById(Long id) {
        return certificateDao.readById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public Certificate updateCertificate(CertificateDTO certificate, Long id) {
        Map<String, String> parameterMap = convertCertificateToParameterMap(certificate);
        CertificateUpdateQueryConfig config = CertificateUpdateQueryConfig.builder()
                .certificateId(id)
                .paramsValueMap(parameterMap)
                .build();
        if (certificateDao.update(config)) {
            return certificateDao.readById(id).orElseThrow(RuntimeException::new);
        } else {
            throw new RuntimeException();
        }
    }

    private static Map<String, String> convertCertificateToParameterMap(CertificateDTO certificate) {
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, String> map = oMapper.convertValue(certificate, Map.class);
        map = map.entrySet().stream().filter(l ->
                l.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return map;
    }

    @Override
    public void deleteCertificate(Long id) {

    }

    @Override
    public List<Certificate> readCertificateByFilterQuery(Optional<String> searchQuery, Optional<String> tagName) {
        return null;
    }
}
