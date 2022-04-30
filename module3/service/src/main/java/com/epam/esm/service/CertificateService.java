package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.builder.CertificatePageSettings;

import java.util.List;

/**
 * CertificateService
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
public interface CertificateService {

    CertificateDto findById(long id);

    List<CertificateDto> findAll(CertificatePageSettings parameters, Pagination pagination);

    CertificateDto create(CertificateDto dto);

    CertificateDto update(CertificateDto dto, long id);

    void delete(long id);
}
