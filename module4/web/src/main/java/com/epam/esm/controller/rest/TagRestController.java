package com.epam.esm.controller.rest;

import com.epam.esm.controller.util.SortTypeMapConverter;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.model.TagModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/tags")
@AllArgsConstructor
public class TagRestController {

    private final TagService tagService;

    private final ModelMapper mapper;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public CollectionModel<TagModel> findAllTags(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size,
                                                 @RequestParam(defaultValue = "-id") String sort) {

        Pageable pageable = PageRequest.of(page, size, SortTypeMapConverter.convert(sort));
        Page<TagModel> tags = tagService.findAll(pageable).map(t -> mapper.map(t, TagModel.class));
        tags.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(TagRestController.class).findAllTags(page, size, sort)).withSelfRel();
        return CollectionModel.of(tags, link);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<TagModel> findTagById(@PathVariable @Positive long id) {
        TagModel tagModel = mapper.map(tagService.findById(id), TagModel.class);
        addSelfRelLink(tagModel);
        return ResponseEntity.ok(tagModel);
    }

    @GetMapping("/popular")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<TagModel> findPopularTagOfRichestUser() {
        TagModel tagModel = mapper.map(tagService.findPopularTagOfRichestUser(), TagModel.class);
        addSelfRelLink(tagModel);
        return ResponseEntity.ok(tagModel);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TagModel> createTag(@RequestBody @Valid TagModel tagModel) {

        Tag tag = mapper.map(tagModel, Tag.class);
        TagModel dto = mapper.map(tagService.create(tag), TagModel.class);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable @Positive Long id) {
        tagService.delete(id);
    }

    private void addSelfRelLink(TagModel tagModel) {
        Link link = linkTo(this.getClass()).slash(tagModel.getId()).withSelfRel();
        tagModel.add(link);
    }
}
