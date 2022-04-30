package com.epam.esm.repository.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.builder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CertificateRepositoryImpl
 *
 * @author alex
 * @version 1.0
 * @since 18.04.22
 */
@Repository
public class CertificateRepositoryImpl extends AbstractRepository<Certificate>
        implements CertificateRepository {

    private static final String SELECT_BY_NAME =
            "Select c FROM Certificate c WHERE c.name=:name";

    private final QueryBuilder<Certificate> certificateQueryBuilder;

    @Autowired
    protected CertificateRepositoryImpl(QueryBuilder<Certificate> certificateQueryBuilder) {
        super(Certificate.class);
        this.certificateQueryBuilder = certificateQueryBuilder;
    }

    @Override
    protected QueryBuilder<Certificate> getQueryBuilder() {
        return this.certificateQueryBuilder;
    }

    @Override
    public Optional<Certificate> findByName(String name) {
        @SuppressWarnings("unchecked")
        List<Certificate> certificates = em.createQuery(SELECT_BY_NAME)
                .setParameter("name", name)
                .getResultList();
        return certificates.stream().findAny();
    }

    @Override
    public boolean isExists(long id) {
        return em.find(Certificate.class, id) != null;
    }
}
