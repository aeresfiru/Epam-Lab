package com.epam.esm.rest;

import com.epam.esm.domain.Certificate;
import com.epam.esm.model.CertificateModel;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.SortTypeMapConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
public class CertificateRestController {

    private final CertificateService service;

    private final ModelMapper mapper;

    @GetMapping
    public CollectionModel<CertificateModel> findAll(@RequestParam(required = false) String query,
                                                     @RequestParam(required = false, value = "tag") List<String> tagNames,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size,
                                                     @RequestParam(defaultValue = "-id") String sort) {

        Pageable pagingSort = PageRequest.of(page, size, SortTypeMapConverter.convert(sort));
        Page<CertificateModel> certificates = service.findAll(query, tagNames, pagingSort)
                .map(this::mapToCertificateModel);

        certificates.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(this.getClass())
                .findAll(query, tagNames, page, size, sort)).withSelfRel().expand();
        return CollectionModel.of(certificates, link);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateModel> findById(@PathVariable @Positive Long id) {
        CertificateModel certificate = mapToCertificateModel(service.findById(id));
        addSelfRelLink(certificate);
        return ResponseEntity.ok(certificate);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EntityModel<CertificateModel> create(@RequestBody @Valid CertificateModel certificateModel) {
        Certificate certificate = mapToCertificate(certificateModel);
        service.create(certificate);
        return EntityModel.of(mapToCertificateModel(certificate));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EntityModel<CertificateModel> update(@RequestBody @Valid CertificateModel certificateModel,
                                                @PathVariable @Positive Long id) {
        Certificate certificate = mapToCertificate(certificateModel);
        certificate = service.update(certificate, id);
        return EntityModel.of(mapToCertificateModel(certificate));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        service.delete(id);
    }

    private void addSelfRelLink(CertificateModel certificateModel) {
        Link link = linkTo(this.getClass()).slash(certificateModel.getId()).withSelfRel();
        certificateModel.add(link);
    }

    private Certificate mapToCertificate(CertificateModel certificateModel) {
        return mapper.map(certificateModel, Certificate.class);
    }

    private CertificateModel mapToCertificateModel(Certificate certificate) {
        return mapper.map(certificate, CertificateModel.class);
    }
}
