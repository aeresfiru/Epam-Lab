package com.epam.esm.service.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.DuplicateEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository repository;

    @InjectMocks
    private TagServiceImpl service;

    private Tag tag;

    @BeforeEach
    void setup() {
        tag = new Tag(1L, "name", new HashSet<>());
    }

    @DisplayName("JUnit test for create tag method")
    @Test
    void givenTagObject_whenSaveTag_thenReturnTagObject() {
        given(repository.findByName(tag.getName())).willReturn(Optional.empty());
        given(repository.save(tag)).willReturn(tag);

        service.create(tag);

        verify(repository, times(1)).save(tag);
    }

    @DisplayName("JUnit test for create Tag which will throw exception")
    @Test
    void givenTagObject_whenSaveTag_thenThrowException() {
        given(repository.findByName(tag.getName())).willReturn(Optional.of(tag));

        assertThatThrownBy(() -> service.create(tag))
                .isInstanceOf(DuplicateEntityException.class);

        verify(repository, never()).save(tag);
    }

    @DisplayName("JUnit test for find Tag by id")
    @Test
    void givenTagId_whenFindById_thenReturnTag() {
        given(repository.findById(tag.getId())).willReturn(Optional.of(tag));

        Tag actual = service.findById(tag.getId());

        assertThat(actual).isNotNull();
    }

    @DisplayName("JUnit test for find Tag by id (negative scenario)")
    @Test
    void givenTagId_whenFindById_thenThrowException() {
        given(repository.findById(tag.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(tag.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("JUnit test for delete Tag method")
    @Test
    void givenTagId_whenDeleteTag_thenNothing() {
        given(repository.existsById(tag.getId())).willReturn(true);
        service.delete(tag.getId());

        verify(repository, times(1)).deleteById(tag.getId());
    }
}
