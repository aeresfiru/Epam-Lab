package com.epam.esm.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.impl.CertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    public final CertificateService service;

    @Autowired
    public CertificateController(CertificateService service) {
        this.service = service;
    }

    @GetMapping
    public List<CertificateDto> readAll() {
        return service.readAllCertificates();
    }

    @GetMapping("/query")
    public List<CertificateDto> readAllBySearchQuery(@RequestParam("search") Optional<String> searchQuery,
                                                     @RequestParam("tag") Optional<List<String>> tagNames,
                                                     @RequestParam("sort") Optional<String> sortingParameters) {
        return service.readCertificateByFilterQuery(searchQuery, tagNames, sortingParameters);
    }

    @GetMapping("/{id}")
    public CertificateDto readById(@PathVariable Long id) {
        return service.readById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDto create(@RequestBody CertificateDto certificate) {
        return service.createCertificate(certificate);
    }

    @PatchMapping("/{id}")
    public CertificateDto update(@RequestBody CertificateDto certificate,
                                 @PathVariable Long id) {
        return service.updateCertificate(certificate, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteCertificate(id);
    }
}
