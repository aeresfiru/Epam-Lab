package com.epam.esm.repository;

import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Optional;

/**
 * TagRepository
 *
 * @author alex
 * @version 1.0
 * @since 16.04.22
 */
public interface TagRepository {

    List<Tag> findAll(Pagination pagination);

    Optional<Tag> findById(long id);

    Optional<Tag> findTheMostPopularTagOfAUserWithAHighestPrice();

    Tag create(Tag tag);

    void delete(long id);

    Optional<Tag> findByName(String tagName);

    boolean isExists(long id);
}
