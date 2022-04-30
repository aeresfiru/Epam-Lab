package com.epam.esm.service.impl;

import com.epam.esm.CertificatesApplication;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.Mapper;
import com.epam.esm.dto.TagDto;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.builder.CertificatePageSettings;
import com.epam.esm.repository.builder.CertificateParameter;
import com.epam.esm.repository.builder.SortType;
import com.epam.esm.repository.impl.CertificateRepositoryImpl;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.IncorrectParameterException;
import com.epam.esm.service.ResourceNotFoundException;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {CertificateRepositoryImpl.class, CertificatesApplication.class})
@ActiveProfiles("test")
class CertificateServiceImplTest {

    private static final Tag TAG_2 = new Tag(2L);

    private static final Certificate CERTIFICATE_1 = new Certificate(1L);
    private static final Certificate CERTIFICATE_2 = new Certificate(2L);
    private static final Certificate CERTIFICATE_3 = new Certificate(3L);

    private CertificateDto CERTIFICATE_1_DTO = new CertificateDto();
    private CertificateDto CERTIFICATE_2_DTO = new CertificateDto();
    private CertificateDto CERTIFICATE_3_DTO = new CertificateDto();

    @Autowired
    private Mapper<Tag, TagDto> tagDtoMapper;

    @Autowired
    private Mapper<Certificate, CertificateDto> certificateDtoMapper;

    @Autowired
    private Validator<TagDto> tagValidator;

    @Autowired
    private Validator<Pagination> paginationValidator;

    @Autowired
    private Validator<CertificateDto> certificateDtoValidator;

    @Mock
    private CertificateRepository certificateRepository;

    private CertificateService certificateService;

    @BeforeEach
    void setup() {
        certificateService = new CertificateServiceImpl(certificateRepository, certificateDtoMapper,
                tagDtoMapper, tagValidator, paginationValidator, certificateDtoValidator);

        CERTIFICATE_1_DTO = certificateDtoMapper.toDto(CERTIFICATE_1);
        CERTIFICATE_2_DTO = certificateDtoMapper.toDto(CERTIFICATE_2);
        CERTIFICATE_3_DTO = certificateDtoMapper.toDto(CERTIFICATE_3);
    }

    @Test
    void whenFindById_thenReturnCertificateDto() {
        when(certificateRepository.findById(CERTIFICATE_1.getId())).thenReturn(Optional.of(CERTIFICATE_1));

        CertificateDto actual = certificateService.findById(CERTIFICATE_1.getId());
        assertEquals(CERTIFICATE_1_DTO, actual);
    }

    @Test
    void whenFindById_thenThrowEntityNotFoundException() {
        long id = -1;
        when(certificateRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> certificateService.findById(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void whenFindAll_thenReturnLimitedListOfCertificateDto(int pageSize) {
        Pagination pagination = new Pagination(1, pageSize);

        List<Certificate> expected = new LinkedList<>();
        for (int i = 0; i < pageSize; i++) {
            expected.add(new Certificate());
        }
        when(certificateRepository.query(CertificatePageSettings.builder().build(), pagination)).thenReturn(expected);

        List<CertificateDto> actual = certificateService.findAll(CertificatePageSettings.builder().build(), pagination);
        assertEquals(pageSize, actual.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4})
    void whenFindAll_thenThrowIncorrectParameterExceptionForPageSize(int pageSize) {
        Pagination pagination = new Pagination(1, pageSize);
        assertThrows(IncorrectParameterException.class,
                () -> certificateService.findAll(CertificatePageSettings.builder().build(), pagination));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4})
    void whenFindAll_thenThrowIncorrectParameterExceptionForPageNumber(int page) {
        Pagination pagination = new Pagination(page, 3);
        assertThrows(IncorrectParameterException.class,
                () -> certificateService.findAll(CertificatePageSettings.builder().build(), pagination));
    }

    @Test
    void whenFindWithParameters_thenReturnListOfCertificateDto() {
        List<Certificate> certificates = Arrays.asList(CERTIFICATE_3, CERTIFICATE_2);

        Map<String, SortType> sortingParameters = new HashMap<>();
        sortingParameters.put(CertificateParameter.NAME.getNameInRequest(), SortType.DESC);
        CertificatePageSettings parameters = CertificatePageSettings.builder()
                .tagParam(Collections.singletonList(TAG_2.getName()))
                .parameterSortingTypeMap(sortingParameters)
                .build();

        Pagination pagination = new Pagination(1, 3);

        when(certificateRepository.query(parameters, pagination)).thenReturn(certificates);

        List<CertificateDto> expected = Arrays.asList(CERTIFICATE_3_DTO, CERTIFICATE_2_DTO);
        List<CertificateDto> actual = certificateService.findAll(parameters, pagination);

        assertEquals(expected, actual);
    }

}