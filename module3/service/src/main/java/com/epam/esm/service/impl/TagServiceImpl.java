package com.epam.esm.service.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.dto.Mapper;
import com.epam.esm.dto.TagDto;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.DuplicateEntityException;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TagServiceImpl
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@Slf4j
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final Mapper<Tag, TagDto> mapper;

    private final Validator<TagDto> tagValidator;

    private final Validator<Pagination> paginationValidator;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
                          Mapper<Tag, TagDto> tagDtoMapper,
                          Validator<TagDto> tagValidator,
                          Validator<Pagination> paginationValidator) {

        this.tagRepository = tagRepository;
        this.mapper = tagDtoMapper;
        this.tagValidator = tagValidator;
        this.paginationValidator = paginationValidator;
    }

    @Override
    public TagDto findById(long id) {
        log.info("IN findTagById - id : {}", id);
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND));
        return mapper.toDto(tag);
    }

    @Override
    public List<TagDto> findAll(Pagination pagination) {
        log.info("IN findAllTags - page : {}, pageSize: {}", pagination.getPage(), pagination.getPageSize());

        paginationValidator.validateForCreate(pagination);

        return tagRepository.findAll(pagination).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto findPopularTagOfRichestUser() {
        log.info("IN findPopularTagOfRichestUser");
        return mapper.toDto(tagRepository.findTheMostPopularTagOfAUserWithAHighestPrice()
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionConstant.RESOURCE_NOT_FOUND)));
    }

    @Override
    @Transactional
    public TagDto create(TagDto dto) {
        log.info("IN createTag, tag: {}", dto);
        tagValidator.validateForCreate(dto);
        this.checkForDuplicate(dto.getName());

        Tag tag = mapper.toEntity(dto);
        tagRepository.create(tag);
        return mapper.toDto(tag);
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("IN delete tag, id: {}", id);
        if (!tagRepository.isExists(id)) {
            throw new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND);
        }
        tagRepository.delete(id);
    }

    private void checkForDuplicate(String tagName) {
        tagRepository.findByName(tagName).ifPresent(tag -> {
            log.info("Failed to create tag with name: '" + tagName + "', tag already exist.");
            throw new DuplicateEntityException(tagName, ExceptionConstant.CERTIFICATE_DUPLICATE);
        });
    }
}
