package com.epam.esm.repository;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CertificateRepositoryTest {

    @Autowired
    private CertificateRepository underTest;


    @BeforeEach
    void setUp() {

    }

    @Test
    void findByName() {
        //given
        Certificate certificate = new Certificate();
        certificate.setName("foo");
        certificate.setStatus(Status.ACTIVE);
        certificate.setDescription("bar");
        certificate.setPrice(new BigDecimal("1.00"));

        underTest.save(certificate);
        //when
        Boolean expected = underTest.exists(Example.of(certificate));

        //then
        assertThat(expected).isTrue();
    }

    @Test
    void findAllByStatus() {
    }
}