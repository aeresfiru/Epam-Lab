package jdbc;

import com.epam.esm.dao.config.DevDatasourceConfig;
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
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DevDatasourceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcCertificateDaoImplTest {

    @Autowired
    private JdbcCertificateDaoImpl certificateDao;
    private Certificate certificate1;
    private Certificate certificate2;
    private Certificate certificate3;
    private Certificate certificate4;

    @BeforeEach
    void setUp() {
        certificate1 = Certificate.builder()
                .id(1L)
                .name("New certificate")
                .description("This is test certificate")
                .price(new BigDecimal("74.99"))
                .duration((short) 31)
                .createDate(LocalDateTime.parse("2022-01-04T20:31"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-04T20:54"))
                .build();
        certificate2 = Certificate.builder()
                .id(11L)
                .name("Sweat couple")
                .description("Romantic breakfast at \"Luigi\" restaurant")
                .price(new BigDecimal("59.99"))
                .duration((short) 10)
                .createDate(LocalDateTime.parse("2022-02-04T10:31"))
                .lastUpdateDate(LocalDateTime.parse("2022-02-04T22:33"))
                .build();
        certificate3 = Certificate.builder()
                .id(12L)
                .name("My only love")
                .description("Spa day for your mother")
                .price(new BigDecimal("259.99"))
                .duration((short) 10)
                .createDate(LocalDateTime.parse("2022-01-04T20:31"))
                .lastUpdateDate(LocalDateTime.parse("2022-01-04T20:54"))
                .build();
        certificate4 = Certificate.builder()
                .id(13L)
                .name("Trampolines")
                .description("Visit trampolines for 5 days")
                .price(new BigDecimal("50.00"))
                .duration((short) 5)
                .createDate(LocalDateTime.parse("2022-04-01T20:50"))
                .lastUpdateDate(LocalDateTime.parse("2022-04-01T20:55"))
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
        Assertions.assertTrue(certificateDao.create(certificate1));
    }

    @Test
    void findByIdTest() {
        Assertions.assertEquals(certificate3, certificateDao.read(12).get());
    }

    @Test
    void findByIdWithInvalidIdTest() {
        Assertions.assertFalse(certificateDao.read(111).isPresent());
    }

    @Test
    void removeCertificatePositiveTest() {
        Assertions.assertTrue(certificateDao.delete(11));
    }

    @Test
    void removeUnknownCertificateTest() {
        Assertions.assertFalse(certificateDao.delete(444));
    }
}