package com.epam.esm.service;

import com.epam.esm.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * TagService
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
public interface TagService {

    Tag findById(Long id);

    Page<Tag> findAll(Pageable pagination);

    Tag findPopularTagOfRichestUser();

    Tag create(Tag tag);

    void delete(Long id);
}
