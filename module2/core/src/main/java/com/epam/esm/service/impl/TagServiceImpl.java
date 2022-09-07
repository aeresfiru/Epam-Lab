package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.validator.TagValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.service.exception.ErrorCode.CERTIFICATE_NOT_FOUND;
import static com.epam.esm.service.exception.ErrorCode.DUPLICATE_TAG_NAME;

@Service
@AllArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    private final DtoMapper<Tag, TagDto> mapper;

    @Override
    public List<TagDto> readAll() {
        log.info("Read all tags method called.");
        return tagDao.readAll().stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto readById(Long id) {
        log.info("Read tag by id method called. Id = {}", id);
        return mapper.mapToDto(tagDao.readById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, CERTIFICATE_NOT_FOUND)));
    }

    @Override
    public TagDto createTag(TagDto dto) {
        log.info("Create tag method called. Name = {}", dto.getName());
        Tag tag = mapper.mapFromDto(dto);
        validateForCreate(tag);
        tagDao.create(tag);
        return mapper.mapToDto(tag);
    }

    private void validateForCreate(Tag tag) {
        TagValidator.validate(tag);
        if (tagDao.readByName(tag.getName()).isPresent()) {
            throw new DuplicateEntityException(tag.getName(), DUPLICATE_TAG_NAME);
        }
    }

    @Override
    public void deleteTag(Long id) {
        log.info("Delete tag method called. Id = {}", id);
        tagDao.delete(id);
    }
}
