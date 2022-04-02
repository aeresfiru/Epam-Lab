package com.epam.esm.jdbc;

import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.jdbc.JdbcTagDaoImpl;
import com.epam.esm.domain.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcTagDaoImplTest {

    @Autowired
    private JdbcTagDaoImpl tagDao;
    private Tag insertTag;
    private Tag tag4;
    private List<Tag> tagsInDB;

    @BeforeEach
    void setUp() {
        insertTag = Tag.builder().name("Tag 1").build();
        Tag tag2 = Tag.builder().id(2L).name("Tag 2").build();
        Tag tag3 = Tag.builder().id(3L).name("Tag 3").build();
        tag4 = Tag.builder().id(4L).name("Tag 4").build();
        Tag tag5 = Tag.builder().id(5L).name("Tag 5").build();
        tagsInDB = Arrays.asList(tag2, tag3, tag4, tag5);
    }

    @Test
    void whenSearchingForAllTags_thenShouldReturnListOfTags() {
        Assertions.assertEquals(tagsInDB, tagDao.readAll());
    }

    @Test
    void whenAddTagThatDoesntExist_thenShouldReturnTrue() {
        Assertions.assertTrue(tagDao.create(insertTag));
    }

    @Test
    void whenSearchingByIdThatExist_thenShouldReturnTagWithThisId() {
        Assertions.assertEquals(tagDao.readById(4L).get(), tag4);
    }

    @Test
    void whenSearchingByIdThatDoesntExist_thenShouldReturnOptionalEmpty() {
        Assertions.assertFalse(tagDao.readById(333L).isPresent());
    }

    @Test
    void whenRemoveTagThatExist_thenShouldReturnTrue() {
        Assertions.assertTrue(tagDao.delete(2L));
    }

    @Test
    void whenRemoveTagThatDoesntExist_thenShouldReturnFalse() {
        Assertions.assertFalse(tagDao.delete(444L));
    }
}