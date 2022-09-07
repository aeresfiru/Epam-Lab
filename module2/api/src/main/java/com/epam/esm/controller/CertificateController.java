package com.epam.esm.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
public class CertificateController {

    public final CertificateService service;

    @GetMapping
    public List<CertificateDto> readAllBySearchQuery(@RequestParam(value = "query", required = false) String searchQuery,
                                                     @RequestParam(value = "tag", required = false) List<String> tagNames,
                                                     @RequestParam(value = "sort", defaultValue = "-id") String sortingParameters) {
        return service.readAllCertificates();
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
