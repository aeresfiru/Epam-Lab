package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.repository.Pagination;

import java.util.List;

/**
 * TagService
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
public interface TagService {

    TagDto findById(long id);

    List<TagDto> findAll(Pagination pagination);

    TagDto findPopularTagOfRichestUser();

    TagDto create(TagDto dto);

    void delete(long id);
}
