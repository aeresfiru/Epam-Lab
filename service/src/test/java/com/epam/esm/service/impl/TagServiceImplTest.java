package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    private Tag firstTag;
    private List<Tag> tags;

    @BeforeEach
    void setUp() {
        firstTag = Tag.builder().id(1L).name("Tag 1").build();
        Tag secondTag = Tag.builder().id(2L).name("Tag 2").build();
        tags = Arrays.asList(firstTag, secondTag);
    }

    @Mock
    private TagDao mockTagRepository;

    @InjectMocks
    private TagServiceImpl service;

    @Test
    void When_TagsPresent_Expect_ReturnListOfTags() {
        Mockito.when(mockTagRepository.readAll()).thenReturn(tags);

        List<Tag> readTags = service.readAll();
        Assertions.assertEquals(tags, readTags);
    }

    @Test
    void When_TagsDoesNotPresent_Expect_ReturnEmptyList() {
        Mockito.when(mockTagRepository.readAll()).thenReturn(Collections.emptyList());

        List<Tag> readTags = service.readAll();
        Assertions.assertEquals(Collections.emptyList(), readTags);
    }

    @Test
    void When_TagWithCertainIdExist_Expect_ReturnTag() {
        Long tagId = 1L;
        Mockito.when(mockTagRepository.readById(tagId)).thenReturn(Optional.of(firstTag));

        Tag tag = service.readById(tagId);
        Assertions.assertEquals(firstTag, tag);
    }

    @Test
    void When_TagWithCertainIdDoesNotExist_Expect_ThrowEntityNotFoundException() {
        Long tagId = 1L;
        Mockito.when(mockTagRepository.readById(tagId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> service.readById(tagId));
    }

    @Test
    void When_DeleteTagThatDoesNotExist_Expect_ThrowEntityNotFoundException() {
        Long tagId = 1L;
        Mockito.when(mockTagRepository.delete(tagId)).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> service.deleteTag(tagId));
    }
}