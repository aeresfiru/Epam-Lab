package com.epam.esm.service.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.DuplicateEntityException;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TagServiceImpl
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@Slf4j
@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public Tag findById(long id) {
        log.info("IN findTagById - id : {}", id);
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND));
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        log.info("IN findAllTags - pageable: {}", pageable);
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag findPopularTagOfRichestUser() {
        log.info("IN findMostUsedTagOfUserWithHighestCostOfAllOrders");
        return tagRepository.findMostUsedTagOfUserWithHighestCostOfAllOrders();
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        log.info("IN createTag, tag: {}", tag);
        this.checkForDuplicate(tag.getName());
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("IN delete tag, id: {}", id);
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND);
        }
        tagRepository.deleteById(id);
    }

    private void checkForDuplicate(String tagName) {
        tagRepository.findByName(tagName).ifPresent(tag -> {
            log.info("Failed to create tag with name: '" + tagName + "', tag already exist.");
            throw new DuplicateEntityException(tagName, ExceptionConstant.CERTIFICATE_DUPLICATE);
        });
    }
}
