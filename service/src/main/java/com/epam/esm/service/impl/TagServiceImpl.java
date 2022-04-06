package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final Validator<Tag> tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, Validator<Tag> tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    @Override
    public List<Tag> readAll() {
        return tagDao.readAll();
    }

    @Override
    public Tag readById(Long id) {
        return tagDao.readById(id).orElseThrow(() -> new EntityNotFoundException(id, ErrorCode.TAG_ERROR));
    }

    @Override
    public Tag createTag(Tag tag) {
        tagValidator.validate(tag);
        if (tagDao.readByName(tag.getName()).isPresent()) {
            throw new DuplicateEntityException(tag.getName(), ErrorCode.TAG_ERROR);
        }
        tagDao.create(tag);
        return tag;
    }

    @Override
    public void deleteTag(Long id) {
        if (!tagDao.delete(id)) {
            throw new EntityNotFoundException(id, ErrorCode.TAG_ERROR);
        }
    }
}
