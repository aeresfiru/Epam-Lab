package com.epam.esm.service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Status;
import com.epam.esm.domain.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.DuplicateEntityException;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    private final TagRepository tagRepository;

    @Override
    public Certificate findById(long id) {
        log.info("IN findById, id: {}", id);
        return certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND));
    }

    @Override
    public Page<Certificate> findAll(Pageable pageable) {
        log.info("IN findAll - pageable: {}", pageable);
        return certificateRepository.findAll(pageable);
    }

    @Override
    public Page<Certificate> findAll(String query, List<String> tagNames, Pageable pageable) {
        log.info("IN findAll - query: {}, tagNames: {}, pageable: {}", query, tagNames, pageable);
        return certificateRepository.findAll(where(certificateNameLike(query)
                .or(certificateDescriptionLike(query)))
                .and(certificateHasTags(tagNames)), pageable);
    }

    @Override
    public Page<Certificate> findAllActiveCertificates(Pageable pageable) {
        log.info("IN findAllActiveCertificates - pageable: {}", pageable);
        return certificateRepository.findAllByStatus(Status.ACTIVE, pageable);
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
    public Certificate create(Certificate certificate) {
        log.info("IN create - creating certificate: {}", certificate);
        checkForDuplicate(certificate.getName());
        Set<Tag> tags = new HashSet<>(certificate.getTags());
        certificate.getTags().clear();
        Certificate created = certificateRepository.save(certificate);
        if (!CollectionUtils.isEmpty(tags)) {
            certificate.setTags(tags);
            created = certificateRepository.save(certificate);
        }
        return created;
    }

    @Override
    @Transactional
    public Certificate update(Certificate certificate, long id) {
        log.info("IN update - updating certificate, id: ({})", id);
        Optional<Certificate> certificateFromDb = certificateRepository.findById(id);
        if (!certificateFromDb.isPresent()) {
            throw new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND);
        }
        this.checkForDuplicate(certificate.getName());
        Certificate toUpdate = certificateFromDb.get();
        updateCertificateFields(toUpdate, certificate);
        return certificateRepository.save(toUpdate);
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("IN delete - deleting certificate: {}", id);
        Optional<Certificate> optional = certificateRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND);
        }
        Certificate certificate = optional.get();
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
