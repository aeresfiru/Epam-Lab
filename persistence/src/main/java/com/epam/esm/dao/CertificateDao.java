package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateDao {

    boolean create(Certificate certificate);

    List<Certificate> readAll();

    Optional<Certificate> read(long id);

    boolean update(Certificate certificate);

    boolean delete(long id);
}
