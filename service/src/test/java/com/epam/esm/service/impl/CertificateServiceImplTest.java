package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.query.config.CertificateSelectQueryConfig;
import com.epam.esm.dao.query.config.CertificateUpdateQueryConfig;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.dto.impl.CertificateDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    private Certificate firstCertificate;
    private Certificate secondCertificate;
    private CertificateDto firstCertificateDto;
    private CertificateDto secondCertificateDto;
    private Set<Tag> firstCertificateTags;
    private Set<Tag> secondCertificateTags;

    private final List<Tag> tags = new ArrayList<Tag>() {{
        add(Tag.builder().id(1L).name("Tag 1").build());
        add(Tag.builder().id(2L).name("Tag 2").build());
        add(Tag.builder().id(3L).name("Tag 3").build());
    }};

    @BeforeEach
    void setUp() {
        firstCertificateTags = new HashSet<Tag>() {{
            add(tags.get(0));
            add(tags.get(1));
        }};
        secondCertificateTags = new HashSet<Tag>() {{
            add(tags.get(1));
            add(tags.get(2));
        }};
        firstCertificate = Certificate.builder()
                .id(1L)
                .name("Certificate 1")
                .description("This is description of 1 certificate")
                .price(new BigDecimal("46.00"))
                .duration((short) 30)
                .createDate(LocalDateTime.parse("2022-02-02T13:40"))
                .lastUpdateDate(LocalDateTime.parse("2022-02-02T13:40"))
                .tags(new HashSet<>(firstCertificateTags))
                .build();
        secondCertificate = Certificate.builder()
                .id(1L)
                .name("Certificate 2")
                .description("This is description of 1 certificate")
                .price(new BigDecimal("46.00"))
                .duration((short) 30)
                .createDate(LocalDateTime.parse("2022-02-02T13:40"))
                .lastUpdateDate(LocalDateTime.parse("2022-02-02T13:40"))
                .tags(new HashSet<>(secondCertificateTags))
                .build();
        firstCertificateDto = new CertificateDto(
                firstCertificate.getId(),
                firstCertificate.getName(),
                firstCertificate.getDescription(),
                firstCertificate.getPrice(),
                firstCertificate.getDuration(),
                firstCertificate.getTags());
        secondCertificateDto = new CertificateDto(
                secondCertificate.getId(),
                secondCertificate.getName(),
                secondCertificate.getDescription(),
                secondCertificate.getPrice(),
                secondCertificate.getDuration(),
                secondCertificate.getTags());
    }

    @Mock
    private CertificateDao mockCertificateDao;

    @Mock
    private TagDao mockTagDao;

    @Mock
    private DtoMapper<Certificate, CertificateDto> dtoMapper;

    @InjectMocks
    private CertificateServiceImpl service;

    @Test
    void When_CreateCertificate_Expect_ReturnCertificate() {
        Mockito.when(mockCertificateDao.readByName(firstCertificate.getName())).thenReturn(Optional.empty());
        Mockito.when(dtoMapper.mapFromDto(firstCertificateDto)).thenReturn(firstCertificate);

        Mockito.when(mockTagDao.readByName(tags.get(0).getName())).thenReturn(Optional.of(tags.get(0)));
        Mockito.when(mockTagDao.readByName(tags.get(1).getName())).thenReturn(Optional.of(tags.get(1)));

        Mockito.when(mockCertificateDao.create(firstCertificate)).thenReturn(true);

        Mockito.when(mockCertificateDao.readById(firstCertificate.getId())).thenReturn(Optional.of(firstCertificate));
        Mockito.when(dtoMapper.mapToDto(firstCertificate)).thenReturn(firstCertificateDto);

        CertificateDto savedCertificate = service.createCertificate(firstCertificateDto);
        Assertions.assertEquals(firstCertificateDto, savedCertificate);
    }

    @Test
    void When_CreateCertificateThatExist_Expect_ThrowDuplicateEntityException() {
        Mockito
                .when(mockCertificateDao.readByName(firstCertificate.getName()))
                .thenReturn(Optional.of(firstCertificate));

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.createCertificate(firstCertificateDto));
    }


    @Test
    void When_FindCertificateByIdThatExist_Expect_ReturnThatCertificate() {
        Mockito.when(mockCertificateDao.readById(firstCertificate.getId())).thenReturn(Optional.of(firstCertificate));
        Mockito.when(mockTagDao.readByCertificateId(firstCertificate.getId())).thenReturn(firstCertificateTags);
        Mockito.when(dtoMapper.mapToDto(firstCertificate)).thenReturn(firstCertificateDto);

        CertificateDto readCertificate = service.readById(firstCertificate.getId());
        Assertions.assertEquals(firstCertificateDto, readCertificate);
    }

    @Test
    void When_FindCertificateByIdThatDoesNotExist_Expect_ThrowEntityNotFoundException() {
        Long id = -22L;
        Mockito.when(mockCertificateDao.readById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> service.readById(-22L));
    }

    @Test
    void When_UpdateCertificate_Expect_ReturnUpdatedCertificate() {
        CertificateUpdateQueryConfig config = CertificateUpdateQueryConfig.builder()
                .certificateId(firstCertificate.getId())
                .build();
        Mockito.lenient().when(mockCertificateDao.update(config)).thenReturn(true);
        Mockito.when(mockCertificateDao.readById(firstCertificate.getId())).thenReturn(Optional.of(firstCertificate));
        Mockito.when(mockTagDao.readByCertificateId(firstCertificate.getId())).thenReturn(firstCertificateTags);
        Mockito.when(mockTagDao.readByName(tags.get(0).getName())).thenReturn(Optional.of(tags.get(0)));
        Mockito.when(mockTagDao.readByName(tags.get(1).getName())).thenReturn(Optional.of(tags.get(1)));

        Mockito.when(dtoMapper.mapFromDto(firstCertificateDto)).thenReturn(firstCertificate);
        Mockito.when(dtoMapper.mapToDto(firstCertificate)).thenReturn(firstCertificateDto);

        CertificateDto savedCertificate = service.updateCertificate(firstCertificateDto, firstCertificate.getId());
        Assertions.assertEquals(firstCertificateDto, savedCertificate);
    }

    @Test
    void When_ReadCertificateByFilter_Expect_ReturnListOfCertificates() {
        List<CertificateDto> certificateDtos = Arrays.asList(firstCertificateDto, secondCertificateDto);
        CertificateSelectQueryConfig config = CertificateSelectQueryConfig.builder()
                .tagParam("Tag")
                .build();
        Mockito.lenient().when(mockCertificateDao.query(config))
                .thenReturn(Arrays.asList(firstCertificate, secondCertificate));

        Mockito.when(mockTagDao.readByCertificateId(firstCertificate.getId())).thenReturn(firstCertificateTags);
        Mockito.when(mockTagDao.readByCertificateId(secondCertificate.getId())).thenReturn(secondCertificateTags);

        Mockito.when(dtoMapper.mapToDto(firstCertificate)).thenReturn(firstCertificateDto);
        Mockito.when(dtoMapper.mapToDto(secondCertificate)).thenReturn(secondCertificateDto);

        List<CertificateDto> certificates = service.readCertificateByFilterQuery(Optional.empty(),
                Optional.of("Tag"), Optional.empty());
        Assertions.assertEquals(certificateDtos, certificates);
    }

}