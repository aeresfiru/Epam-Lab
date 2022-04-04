package com.epam.esm.jdbc;

import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.builder.*;
import com.epam.esm.dao.builder.select.CertificateSelectQueryConfig;
import com.epam.esm.dao.builder.update.CertificateUpdateQueryConfig;
import com.epam.esm.dao.jdbc.JdbcCertificateDaoImpl;
import com.epam.esm.domain.Certificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcCertificateDaoImplTest {

    @Autowired
    private JdbcCertificateDaoImpl certificateDao;

    private Certificate certificateToInsert;
    private Certificate certificate2;
    private Certificate certificate3;
    private Certificate certificate4;

    @BeforeEach
    void setUp() {
        certificateToInsert = Certificate.builder()
                .name("Certificate 1")
                .description("This is 1 certificate description")
                .price(new BigDecimal("100.00"))
                .duration((short) 3)
                .createDate(LocalDateTime.parse("2022-04-01T00:01"))
                .lastUpdateDate(LocalDateTime.parse("2022-04-01T00:02"))
                .build();
        certificate2 = Certificate.builder()
                .id(2L)
                .name("Certificate 2")
                .description("This is 2 certificate description")
                .price(new BigDecimal("100.00"))
                .duration((short) 3)
                .createDate(LocalDateTime.parse("2022-04-01T00:01"))
                .lastUpdateDate(LocalDateTime.parse("2022-04-01T00:02"))
                .build();
        certificate3 = Certificate.builder()
                .id(3L)
                .name("Certificate 3")
                .description("This is 3 certificate description")
                .price(new BigDecimal("100.00"))
                .duration((short) 3)
                .createDate(LocalDateTime.parse("2022-04-01T00:01"))
                .lastUpdateDate(LocalDateTime.parse("2022-04-01T00:02"))
                .build();
        certificate4 = Certificate.builder()
                .id(4L)
                .name("Certificate 4")
                .description("This is 4 certificate description")
                .price(new BigDecimal("100.00"))
                .duration((short) 3)
                .createDate(LocalDateTime.parse("2022-04-01T00:01"))
                .lastUpdateDate(LocalDateTime.parse("2022-04-01T00:02"))
                .build();
    }

    @Test
    void When_SearchingForAllCertificates_Expect_ReturnListOfCertificates() {
        List<Certificate> certificates =
                Arrays.asList(certificate2, certificate3, certificate4);
        Assertions.assertEquals(certificates, certificateDao.readAll());
    }

    @Test
    void When_AddCertificateThatDoesntExist_Expect_ReturnTrue() {
        Assertions.assertTrue(certificateDao.create(certificateToInsert));
    }

    @Test
    void When_UpdateCertificateThatExist_Expect_ReturnTrue() {
        certificate4.setName("Pasha is gay");
        certificate4.setDuration((short) 10);
        Map<String, String> params = new HashMap<>();
        params.put("name", certificate4.getName());
        params.put("duration", String.valueOf(certificate4.getDuration()));
        CertificateUpdateQueryConfig config = CertificateUpdateQueryConfig
                .builder()
                .paramsValueMap(params)
                .certificateId(certificate4.getId())
                .build();
        Assertions.assertTrue(certificateDao.update(config));
    }


    @Test
    void When_SearchingByIdThatExist_Expect_ReturnCertificateWithThisId() {
        Assertions.assertEquals(certificate3, certificateDao.readById(3L).get());
    }

    @Test
    void When_SearchingByIdThatDoesntExist_Expect_ReturnOptionalEmpty() {
        Assertions.assertFalse(certificateDao.readById(111L).isPresent());
    }

    @Test
    void When_RemoveCertificateThatExist_Expect_ReturnTrue() {
        Assertions.assertTrue(certificateDao.delete(2L));
    }

    @Test
    void When_RemoveCertificateThatDoesntExist_Expect_ReturnFalse() {
        Assertions.assertFalse(certificateDao.delete(444L));
    }

    @Test
    void When_QueryFoundEntities_Expect_ReturnNotEmptyList() {
        CertificateSelectQueryConfig config = CertificateSelectQueryConfig.builder().tagParam("Tag 2").searchQuery("2").build();
        List<Certificate> certificates = certificateDao.query(config);
        Assertions.assertFalse(certificates.isEmpty());
    }

    @Test
    void When_QueryNothingFound_Expect_ReturnEmptyCollection() {
        Map<String, SortingType> paramSortMap = new HashMap<>();
        paramSortMap.put("c.name", SortingType.ASC);
        CertificateSelectQueryConfig config = CertificateSelectQueryConfig.builder()
                .parameterSortingTypeMap(paramSortMap)
                .tagParam("Tag 256")
                .build();
        List<Certificate> certificates = certificateDao.query(config);
        Assertions.assertTrue(certificates.isEmpty());
    }

    @Test
    void When_AttachTagSuccess_Expect_ReturnTrue() {
        long tagId = 3L;
        long certificateId = certificate2.getId();
        Assertions.assertTrue(certificateDao.attachTagToCertificate(certificateId, tagId));
    }

    @Test
    void When_DetachTagSuccess_Expect_ReturnTrue() {
        long tagId = 2L;
        long certificateId = certificate2.getId();
        Assertions.assertTrue(certificateDao.detachTagFromCertificate(certificateId, tagId));
    }
}