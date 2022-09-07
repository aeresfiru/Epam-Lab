package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;

import java.util.List;

public interface TagService {

    List<TagDto> readAll();

    TagDto readById(Long id);

    TagDto createTag(TagDto dto);

    void deleteTag(Long id);
}
