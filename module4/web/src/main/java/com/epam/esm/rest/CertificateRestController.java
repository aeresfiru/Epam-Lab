package com.epam.esm.rest;

import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateCreateDto;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateUpdateDto;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.SortTypeMapConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public CollectionModel<CertificateDto> findAll(@RequestParam(required = false) String query,
                                                   @RequestParam(required = false, value = "tag") List<String> tagNames,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size,
                                                   @RequestParam(defaultValue = "-id") String sort) {

        Pageable pagingSort = PageRequest.of(page, size, SortTypeMapConverter.convert(sort));
        Page<CertificateDto> certificates = service.findAll(query, tagNames, pagingSort)
                .map(c -> mapper.map(c, CertificateDto.class));

        certificates.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(this.getClass())
                .findAll(query, tagNames, page, size, sort)).withSelfRel().expand();
        return CollectionModel.of(certificates, link);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> findById(@PathVariable @Positive Long id) {
        CertificateDto certificate = mapper.map(service.findById(id), CertificateDto.class);
        addSelfRelLink(certificate);
        return ResponseEntity.ok(certificate);
    }

    @PostMapping
    public ResponseEntity<CertificateDto> create(@RequestBody @Valid CertificateCreateDto toCreate) {
        Certificate certificate = service.create(mapper.map(toCreate, Certificate.class));
        return new ResponseEntity<>(mapper.map(certificate, CertificateDto.class), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CertificateDto> update(@RequestBody @Valid CertificateUpdateDto toUpdate,
                                                 @PathVariable @Positive Long id) {
        Certificate certificate = service.update(mapper.map(toUpdate, Certificate.class), id);
        return new ResponseEntity<>(mapper.map(certificate, CertificateDto.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        service.delete(id);
    }

    private void addSelfRelLink(CertificateDto certificateDto) {
        Link link = linkTo(this.getClass()).slash(certificateDto.getId()).withSelfRel();
        certificateDto.add(link);
    }
}
