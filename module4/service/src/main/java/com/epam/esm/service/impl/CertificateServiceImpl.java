package com.epam.esm.service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Status;
import com.epam.esm.domain.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.DuplicateEntityException;
import com.epam.esm.service.ExceptionConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static com.epam.esm.repository.specification.CertificateSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * CertificateServiceImpl
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Slf4j
@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;

    @Override
    public Certificate findById(Long id) {
        log.info("IN findById, id: {}", id);
        return certificateRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<Certificate> findAll(String query, List<String> tagNames, Pageable pageable) {
        log.info("IN findAll - query: {}, tagNames: {}, pageable: {}", query, tagNames, pageable);
        return certificateRepository.findAll(where(certificateNameLike(query)
                .or(certificateDescriptionLike(query)))
                .and(certificateHasTags(tagNames)), pageable);
    }

    @Override
    public Page<Certificate> findAllActiveCertificates(String query, List<String> tagNames, Pageable pageable) {
        log.info("IN findAll - query: {}, tagNames: {}, pageable: {}", query, tagNames, pageable);
        return certificateRepository.findAll((where(certificateNameLike(query)
                .or(certificateDescriptionLike(query)))
                .and(certificateStatusIs(Status.ACTIVE)))
                .and(certificateHasTags(tagNames)), pageable);
    }

    @Override
    @Transactional
    public void create(Certificate certificate) {
        log.info("IN create - creating certificate: {}", certificate);
        checkForDuplicate(certificate.getName());
        Set<Tag> tags = new HashSet<>(certificate.getTags());
        certificate.getTags().clear();
        certificateRepository.save(certificate);
        if (!CollectionUtils.isEmpty(tags)) {
            certificate.setTags(tags);
            certificateRepository.save(certificate);
        }
    }

    @Override
    @Transactional
    public Certificate update(Certificate certificate, Long id) {
        log.info("IN update - updating certificate, id: ({})", id);
        Certificate toUpdate = certificateRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        this.checkForDuplicate(certificate.getName());
        updateCertificateFields(toUpdate, certificate);
        return certificateRepository.save(toUpdate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("IN delete - deleting certificate: {}", id);
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        certificate.setStatus(Status.DELETED);
        certificateRepository.save(certificate);
    }

    private void updateCertificateFields(Certificate certificateToUpdate, Certificate certificate) {
        if (Objects.nonNull(certificate.getName()) && !"".equalsIgnoreCase(certificate.getName())) {
            certificateToUpdate.setName(certificate.getName());
        }
        if (Objects.nonNull(certificate.getDescription()) && !"".equalsIgnoreCase(certificate.getDescription())) {
            certificateToUpdate.setDescription(certificate.getDescription());
        }
        if (Objects.nonNull(certificate.getPrice())) {
            certificateToUpdate.setPrice(certificate.getPrice());
        }
        if (Objects.nonNull(certificate.getDuration())) {
            certificateToUpdate.setDuration(certificate.getDuration());
        }
        if (certificate.getTags() != null) {
            certificateToUpdate.setTags(certificate.getTags());
        }
    }

    private void checkForDuplicate(String certificateName) {
        if (certificateName != null) {
            certificateRepository.findByName(certificateName).ifPresent(tag -> {
                throw new DuplicateEntityException(certificateName, ExceptionConstant.CERTIFICATE_DUPLICATE);
            });
        }
    }
}
