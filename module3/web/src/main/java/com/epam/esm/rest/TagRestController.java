package com.epam.esm.rest;

import com.epam.esm.dto.TagDto;
import com.epam.esm.repository.Pagination;
import com.epam.esm.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagRestController {

    private final TagService tagService;

    @GetMapping
    public CollectionModel<TagDto> findAllTags(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize) {

        List<TagDto> tags = tagService.findAll(new Pagination(page, pageSize));
        tags.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(TagRestController.class)
                .findAllTags(page, pageSize)).withSelfRel();
        return CollectionModel.of(tags, link);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findTagById(@PathVariable long id) {
        TagDto tagDto = tagService.findById(id);
        addSelfRelLink(tagDto);
        return ResponseEntity.ok(tagDto);
    }

    @GetMapping("/popular")
    public ResponseEntity<TagDto> findPopularTagOfRichestUser() {
        TagDto tagDto = tagService.findPopularTagOfRichestUser();
        addSelfRelLink(tagDto);
        return ResponseEntity.ok(tagDto);
    }

    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tag) {
        TagDto tagDto = tagService.create(tag);
        return new ResponseEntity<>(tagDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }

    private void addSelfRelLink(TagDto tagDto) {
        Link link = linkTo(this.getClass()).slash(tagDto.getId()).withSelfRel();
        tagDto.add(link);
    }
}
