package com.epam.esm.service;

import com.epam.esm.service.dto.impl.CertificateDto;

import java.util.List;
import java.util.Optional;

public interface CertificateService {

    CertificateDto createCertificate(CertificateDto certificate);

    List<CertificateDto> readAllCertificates();

    CertificateDto readById(Long id);

    CertificateDto updateCertificate(CertificateDto certificate, Long id);

    void deleteCertificate(Long id);

    List<CertificateDto> readCertificateByFilterQuery(Optional<String> searchQuery,
                                                   Optional<String> tagName,
                                                      Optional<List<String>> sortTypes);
}
