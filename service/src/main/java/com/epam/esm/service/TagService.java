package com.epam.esm.service;

import com.epam.esm.domain.Tag;

import java.util.List;

public interface TagService {

    List<Tag> readAll();

    Tag readById(Long id);

    Tag createTag(Tag tag);

    void deleteTag(Long id);
}
