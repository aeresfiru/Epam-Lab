package com.epam.esm.repository;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CertificateRepository
 *
 * @author alex
 * @version 1.0
 * @since 7.05.22
 */
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long>,
        JpaSpecificationExecutor<Certificate> {

    Optional<Certificate> findByName(String name);

    Page<Certificate> findAllByStatus(Status status, Pageable pageable);
}
