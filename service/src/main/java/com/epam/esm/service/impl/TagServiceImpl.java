package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validator.AbstractValidator;
import com.epam.esm.service.validator.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    private final AbstractValidator<Tag> validator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, AbstractValidator<Tag> validator) {
        this.tagDao = tagDao;
        this.validator = validator;
    }

    @Override
    public List<Tag> readAll() {
        return tagDao.readAll();
    }

    @Override
    public Tag readById(Long id) {
        return tagDao.readById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.TAG_ERROR, id));
    }

    @Override
    public Tag createTag(Tag tag) {
        if (!validator.validate(tag).isEmpty()) {
            throw new RuntimeException("Validation exception");
        }

        if (!tagDao.create(tag)) {
            throw new ServiceException("Bad request");
        }

        return tag;
    }

    @Override
    public void deleteTag(Long id) {
        if (!tagDao.delete(id)) {
            throw new ServiceException("Requested source not found (id =%s)");
        }
    }
}
