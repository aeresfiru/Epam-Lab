package com.epam.esm.service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Status;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.service.DuplicateEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    @Mock
    private CertificateRepository repository;

    @InjectMocks
    private CertificateServiceImpl service;

    private Certificate certificate;

    @BeforeEach
    void setup() {
        certificate = new Certificate(1L, "name", "desc", new BigDecimal("5.00"), (short) 90, null, null, Status.ACTIVE, new HashSet<>(), new HashSet<>());
    }

    @DisplayName("JUnit test for create certificate method")
    @Test
    void givenCertificateObject_whenSaveCertificate_thenReturnCertificateObject() {
        given(repository.findByName(certificate.getName())).willReturn(Optional.empty());
        given(repository.save(certificate)).willReturn(certificate);

        service.create(certificate);

        verify(repository, times(1)).save(certificate);
    }

    @DisplayName("JUnit test for create certificate which will throw exception")
    @Test
    void givenCertificateObject_whenSaveCertificate_thenThrowException() {
        given(repository.findByName(certificate.getName())).willReturn(Optional.of(certificate));

        assertThatThrownBy(() -> service.create(certificate))
                .isInstanceOf(DuplicateEntityException.class);

        verify(repository, never()).save(certificate);
    }

    @DisplayName("JUnit test for find certificate by id")
    @Test
    void givenCertificateId_whenFindById_thenReturnCertificate() {
        given(repository.findById(certificate.getId())).willReturn(Optional.of(certificate));

        Certificate actual = service.findById(certificate.getId());

        assertThat(actual).isNotNull();
    }

    @DisplayName("JUnit test for find certificate by id (negative scenario)")
    @Test
    void givenCertificateId_whenFindById_thenThrowException() {
        given(repository.findById(certificate.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(certificate.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("JUnit test for update certificate method")
    @Test
    void givenCertificateObject_whenUpdateCertificate_thenReturnUpdatedCertificate() {
        given(repository.save(any(Certificate.class))).willReturn(certificate);
        given(repository.findById(certificate.getId())).willReturn(Optional.of(certificate));

        certificate.setName("Another name");

        Certificate actual = service.update(certificate, certificate.getId());

        assertThat(actual.getName()).isEqualTo("Another name");
    }

    @DisplayName("JUnit test for delete certificate method")
    @Test
    void givenCertificateId_whenDeleteCertificate_thenChangeCertificateStatus() {
        given(repository.findById(certificate.getId())).willReturn(Optional.of(certificate));
        service.delete(certificate.getId());
        assertThat(certificate.getStatus()).isEqualTo(Status.DELETED);
    }
}
