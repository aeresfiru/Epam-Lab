package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    public final CertificateService service;

    @Autowired
    public CertificateController(CertificateService service) {
        this.service = service;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Certificate> readAll() {
        return service.readAllCertificates();
    }

    @GetMapping("/query")
    @ResponseStatus(HttpStatus.OK)
    public List<Certificate> readAllBySearchQuery(@RequestParam Optional<String> searchQuery,
                                                  @RequestParam Optional<String> tagName) {
        return service.readCertificateByFilterQuery(searchQuery, tagName);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Certificate readById(@PathVariable Long id) {
        return service.readById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Certificate create(@RequestBody Certificate certificate) {
        return service.createCertificate(certificate);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Certificate update(@RequestBody CertificateDTO certificate,
                              @PathVariable Long id) {
        return service.updateCertificate(certificate, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteCertificate(id);
    }
}
