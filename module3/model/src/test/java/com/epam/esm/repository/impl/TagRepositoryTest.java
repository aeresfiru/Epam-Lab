package com.epam.esm.repository.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TagRepositoryTest {

    private Tag tag1;
    private final Tag tag2 = new Tag(2L);
    private final Tag tag3 = new Tag(3L);
    private final Tag tagToCreate = new Tag();

    private final TagRepository tagRepository;

    @Autowired
    TagRepositoryTest(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @BeforeEach
    void setUp() {
        tag1 = new Tag(1L);
    }

    @Test
    void whenFindAll_thenReturnAllTags() {
        List<Tag> expected = Arrays.asList(tag1, tag2, tag3);
        List<Tag> actual = tagRepository.findAll(new Pagination(1, 3));

        assertEquals(expected, actual);
    }

    @Test
    void whenFindById_thenReturnTagWithThatId() {
        Optional<Tag> expected = Optional.of(tag1);
        Optional<Tag> actual = tagRepository.findById(expected.get().getId());

        assertEquals(expected, actual);
    }

    @Test
    void whenGetTheMostPopularTagOfAUserWithAHighestPrice_thenReturnIt() {
        Optional<Tag> expected = Optional.of(tag1);
        Optional<Tag> actual = tagRepository.findTheMostPopularTagOfAUserWithAHighestPrice();

        assertEquals(expected, actual);
    }

    @Test
    void whenCreate_thenDoesNotThrowException() {
        assertDoesNotThrow(() -> tagRepository.create(tagToCreate));
    }

    @Test
    void whenDelete_thenDoesNotThrowException() {
        assertDoesNotThrow(() -> tagRepository.delete(tag1.getId()));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void whenIsExistsById_thenReturnTrue(long id) {
        boolean actual = tagRepository.isExists(id);
        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, -2, -3, -4, -5})
    void whenIsExistsById_thenReturnFalse(long id) {
        boolean actual = tagRepository.isExists(id);
        assertFalse(actual);
    }
}