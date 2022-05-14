package com.epam.esm.rest;

import com.epam.esm.domain.Tag;
import com.epam.esm.model.TagModel;
import com.epam.esm.service.TagService;
import com.epam.esm.util.SortTypeMapConverter;
import lombok.AllArgsConstructor;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagRestController {

    private final TagService tagService;

    private final ModelMapper mapper;

    @GetMapping
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
    public ResponseEntity<TagModel> findTagById(@PathVariable @Positive long id) {
        TagModel tagModel = mapper.map(tagService.findById(id), TagModel.class);
        addSelfRelLink(tagModel);
        return ResponseEntity.ok(tagModel);
    }

    @GetMapping("/popular")
    public ResponseEntity<TagModel> findPopularTagOfRichestUser() {
        TagModel tagModel = mapper.map(tagService.findPopularTagOfRichestUser(), TagModel.class);
        addSelfRelLink(tagModel);
        return ResponseEntity.ok(tagModel);
    }

    @PostMapping
    public ResponseEntity<TagModel> createTag(@RequestBody @Valid TagModel tagModel) {

        Tag tag = mapper.map(tagModel, Tag.class);
        TagModel dto = mapper.map(tagService.create(tag), TagModel.class);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        tagService.delete(id);
    }

    private void addSelfRelLink(TagModel tagModel) {
        Link link = linkTo(this.getClass()).slash(tagModel.getId()).withSelfRel();
        tagModel.add(link);
    }
}
