package jdbc;

import com.epam.esm.dao.jdbc.JdbcCertificateDaoImpl;
import com.epam.esm.domain.Certificate;
import config.TestConfig;
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
import java.util.List;

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
    void findAllCertificatesTest() {
        List<Certificate> certificates =
                Arrays.asList(certificate2, certificate3, certificate4);
        Assertions.assertEquals(certificates, certificateDao.readAll());
    }

    @Test
    void addCertificateTest() {
        Assertions.assertTrue(certificateDao.create(certificateToInsert));
    }

    @Test
    void findByIdTest() {
        Assertions.assertEquals(certificate3, certificateDao.read(3L).get());
    }

    @Test
    void findByIdWithInvalidIdTest() {
        Assertions.assertFalse(certificateDao.read(111L).isPresent());
    }

    @Test
    void removeCertificatePositiveTest() {
        Assertions.assertTrue(certificateDao.delete(2L));
    }

    @Test
    void removeUnknownCertificateTest() {
        Assertions.assertFalse(certificateDao.delete(444L));
    }
}