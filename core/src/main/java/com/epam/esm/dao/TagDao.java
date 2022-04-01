package com.epam.esm.dao;

import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {

    boolean create(Tag tag);

    List<Tag> readAll();

    Optional<Tag> read(long id);

    boolean delete(long id);
}
