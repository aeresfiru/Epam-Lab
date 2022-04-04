package com.epam.esm.service;

import com.epam.esm.domain.Certificate;
import com.epam.esm.service.dto.CertificateDTO;

import java.util.List;
import java.util.Optional;

public interface CertificateService {

    Certificate createCertificate(Certificate certificate);

    List<Certificate> readAllCertificates();

    Certificate readById(Long id);

    Certificate updateCertificate(CertificateDTO certificate, Long id);

    void deleteCertificate(Long id);

    List<Certificate> readCertificateByFilterQuery(Optional<String> searchQuery,
                                                   Optional<String> tagName);
}
