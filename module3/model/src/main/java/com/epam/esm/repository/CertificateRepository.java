package com.epam.esm.repository;

import com.epam.esm.domain.Certificate;
import com.epam.esm.repository.builder.PageSettings;

import java.util.List;
import java.util.Optional;

/**
 * CertificateRepository
 *
 * @author alex
 * @version 1.0
 * @since 18.04.22
 */
public interface CertificateRepository {

    Optional<Certificate> findById(long id);

    Certificate create(Certificate certificate);

    Certificate update(Certificate certificate);

    void delete(long id);

    List<Certificate> query(PageSettings parameters, Pagination pagination);

    Optional<Certificate> findByName(String name);

    boolean isExists(long id);
}
