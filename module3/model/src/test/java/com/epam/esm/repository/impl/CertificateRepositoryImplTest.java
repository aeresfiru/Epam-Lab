package com.epam.esm.repository.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.builder.CertificatePageSettings;
import com.epam.esm.repository.builder.CertificateParameter;
import com.epam.esm.repository.builder.PageSettings;
import com.epam.esm.repository.builder.SortType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
class CertificateRepositoryImplTest {

    private static final Tag TAG_1 = new Tag(1L);
    private static final Tag TAG_2 = new Tag(2L);
    private static final Tag TAG_3 = new Tag(3L);

    private static final Certificate CERTIFICATE_1 = new Certificate(1L);
    private static final Certificate CERTIFICATE_2 = new Certificate(2L);
    private static final Certificate CERTIFICATE_3 = new Certificate(3L);

    private static final Certificate CERTIFICATE_TO_CREATE = new Certificate();

    private final CertificateRepository certificateRepository;

    @Autowired
    CertificateRepositoryImplTest(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Test
    void whenFindById_thenReturnCertificateWithThatId() {
        Optional<Certificate> expected = Optional.of(CERTIFICATE_1);
        Optional<Certificate> actual = certificateRepository.findById(CERTIFICATE_1.getId());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenUpdate_thenDoesNotThrowException() {
        CERTIFICATE_1.setDescription("New description");
        Assertions.assertDoesNotThrow(() -> certificateRepository.update(CERTIFICATE_1));
    }

    @Test
    void whenDelete_thenDoesNotThrowException() {
        Assertions.assertDoesNotThrow(() -> certificateRepository.delete(1L));
    }
}