package com.epam.esm.service;

import com.epam.esm.domain.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * CertificateService
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
public interface CertificateService {

    Certificate findById(Long id);

    Page<Certificate> findAll(String query, List<String> tagNames, Pageable pageable);

    Page<Certificate> findAllActiveCertificates(String query, List<String> tagNames, Pageable pageable);

    void create(Certificate certificate);

    Certificate update(Certificate certificate, Long id);

    void delete(Long id);
}
