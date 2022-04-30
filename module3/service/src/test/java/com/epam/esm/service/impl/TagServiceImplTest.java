package com.epam.esm.service.impl;

import com.epam.esm.CertificatesApplication;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.Mapper;
import com.epam.esm.dto.TagDto;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.DuplicateEntityException;
import com.epam.esm.service.IncorrectParameterException;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {TagRepositoryImpl.class, CertificatesApplication.class})
@ActiveProfiles("test")
class TagServiceImplTest {

    private TagDto TAG_1_DTO;
    private TagDto TAG_TO_CREATE_DTO;

    private static final Tag TAG_1 = new Tag(1L);
    private static final Tag TAG_TO_CREATE = new Tag(101L);

    @Autowired
    private Mapper<Tag, TagDto> mapper;

    @Autowired
    private Validator<TagDto> tagValidator;

    @Autowired
    private Validator<Pagination> paginationValidator;

    @Mock
    private TagRepository tagRepository;

    private TagService tagService;

    @BeforeEach
    void setup() {
        tagService = new TagServiceImpl(tagRepository, mapper, tagValidator, paginationValidator);
        TAG_1_DTO = mapper.toDto(TAG_1);
        TAG_TO_CREATE_DTO = mapper.toDto(TAG_TO_CREATE);
    }

    @Test
    void whenFindById_thenReturnTagDto() {
        when(tagRepository.findById(TAG_1.getId())).thenReturn(Optional.of(TAG_1));

        TagDto actual = tagService.findById(TAG_1.getId());
        assertEquals(TAG_1_DTO, actual);
    }

    @Test
    void whenFindById_thenThrowEntityNotFoundException() {
        long id = -1;
        when(tagRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tagService.findById(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void whenFindAll_thenReturnLimitedListOfTagDto(int pageSize) {
        Pagination pagination = new Pagination(1, pageSize);

        List<Tag> expected = new LinkedList<>();
        for (int i = 0; i < pageSize; i++) {
            expected.add(new Tag());
        }
        when(tagRepository.findAll(pagination)).thenReturn(expected);

        List<TagDto> actual = tagService.findAll(pagination);
        assertEquals(pageSize, actual.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4})
    void whenFindAll_thenThrowIncorrectParameterExceptionForPageSize(int pageSize) {
        Pagination pagination = new Pagination(1, pageSize);
        assertThrows(IncorrectParameterException.class,
                () -> tagService.findAll(pagination));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4})
    void whenFindAll_thenThrowIncorrectParameterExceptionForPageNumber(int page) {
        Pagination pagination = new Pagination(page, 3);
        assertThrows(IncorrectParameterException.class,
                () -> tagService.findAll(pagination));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "1"})
    void whenCreate_thenThrowIncorrectParameterException(String name) {
        TAG_TO_CREATE_DTO.setName(name);
        assertThrows(IncorrectParameterException.class,
                () -> tagService.create(TAG_TO_CREATE_DTO));
    }

    @Test
    void whenDelete_thenNotThrowException() {
        long id = 1;
        when(tagRepository.isExists(id)).thenReturn(true);
        doNothing().when(tagRepository).delete(id);
        assertDoesNotThrow(() -> tagService.delete(id));
    }

    @Test
    void whenDelete_thenThrowEntityNotFoundException() {
        long id = -1;
        when(tagRepository.isExists(id)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,
                () -> tagService.delete(id));
    }

    @Test
    void testGetMostPopularTagOfUserWithHighestCostOfAllOrders() {
        long userId = 1;
        when(tagRepository.findTheMostPopularTagOfAUserWithAHighestPrice()).thenReturn(Optional.of(TAG_1));

        TagDto actual = tagService.findPopularTagOfRichestUser();
        TagDto expected = TAG_1_DTO;

        assertEquals(expected, actual);
    }
}