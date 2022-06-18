package com.epam.esm.service.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.DuplicateEntityException;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

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
    public Tag findById(Long id) {
        log.info("Searching for tag by id: {}", id);
        return tagRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        log.info("Searching for all tags - pageable: {}", pageable);
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag findPopularTagOfRichestUser() {
        log.info("Searching for popular tag");
        return tagRepository.findMostUsedTagOfUserWithHighestCostOfAllOrders();
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        log.info("Creating tag: {}", tag);
        this.checkForDuplicate(tag.getName());
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting tag, id: {}", id);
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException();
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
