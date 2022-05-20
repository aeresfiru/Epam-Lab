package com.epam.esm.rest;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.builder.CertificatePageSettings;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.SortTypeMapConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
public class CertificateRestController {

    public final CertificateService service;

    @GetMapping
    public CollectionModel<CertificateDto> findAll(@RequestParam(value = "query") Optional<String> query,
                                                   @RequestParam(value = "tag") Optional<List<String>> tagNames,
                                                   @RequestParam(defaultValue = "-id") String sort,
                                                   @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                   @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize) {

        List<CertificateDto> certificates =
                service.findAll(buildQueryParameters(tagNames, sort, query), new Pagination(page, pageSize));
        certificates.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(this.getClass())
                .findAll(query, tagNames, sort, page, pageSize)).withSelfRel().expand();
        return CollectionModel.of(certificates, link);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> findById(@PathVariable Long id) {
        CertificateDto certificateDto = service.findById(id);
        addSelfRelLink(certificateDto);
        return ResponseEntity.ok(certificateDto);
    }

    @PostMapping
    public ResponseEntity<CertificateDto> create(@RequestBody CertificateDto certificate) {
        CertificateDto certificateDto = service.create(certificate);
        return new ResponseEntity<>(certificateDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CertificateDto> update(@RequestBody CertificateDto certificate,
                                                 @PathVariable Long id) {
        CertificateDto certificateDto = service.update(certificate, id);
        return new ResponseEntity<>(certificateDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    private CertificatePageSettings buildQueryParameters(Optional<List<String>> tags,
                                                         String sort,
                                                         Optional<String> search) {
        return CertificatePageSettings.builder()
                .searchQuery(search.orElse(null))
                .tagParam(tags.orElse(null))
                .parameterSortingTypeMap(SortTypeMapConverter.convert(sort))
                .build();
    }

    private void addSelfRelLink(CertificateDto certificateDto) {
        Link link = linkTo(this.getClass()).slash(certificateDto.getId()).withSelfRel();
        certificateDto.add(link);
    }
}
