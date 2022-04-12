package com.epam.esm.service.validator;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.dto.impl.CertificateDto;
import com.epam.esm.service.exception.IncorrectParameterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

class CertificateValidatorTest {

    private CertificateDto certificate;

    @BeforeEach
    public void setupCertificate() {
        certificate = CertificateDto.builder()
                .id(101L)
                .name("This is a good name")
                .description("This is a good description")
                .price(new BigDecimal(10))
                .duration((short) 31)
                .createDate(LocalDateTime.parse("2022-02-02T22:22"))
                .lastUpdateDate(LocalDateTime.parse("2022-02-02T22:22"))
                .tags(new HashSet<>(Collections.singletonList(Tag.builder().name("Not empty tag list").build())))
                .build();
    }

    @Test
    void When_CertificateValid_Expect_NotThrowsIncorrectParameterException() {
        Assertions.assertDoesNotThrow(() -> CertificateValidator.validate(certificate));
    }

    @Test
    void When_NameLessThanThreeCharactersLong_Expect_ThrowsIncorrectParameterException() {
        certificate.setName("tw");
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> CertificateValidator.validate(certificate));
    }

    @Test
    void When_NameMoreThan100CharsLong_Expect_ThrowsIncorrectParameterException() {
        String spaces = new String(new char[101]).replace('\0', ' ');
        certificate.setName(spaces);
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> CertificateValidator.validate(certificate));
    }

    @Test
    void When_EmptyName_Expect_ThrowsIncorrectParameterException() {
        certificate.setName("");
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> CertificateValidator.validate(certificate));
    }

    @Test
    void When_DescLessThanTwentyCharsLong_Expect_ThrowsIncorrectParameterException() {
        certificate.setDescription("Bad description");
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> CertificateValidator.validate(certificate));
    }

    @Test
    void When_DescMoreThan500CharsLong_Expect_ThrowsIncorrectParameterException() {
        String spaces = new String(new char[501]).replace('\0', ' ');
        certificate.setDescription(spaces);
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> CertificateValidator.validate(certificate));
    }

    @Test
    void When_EmptyDesc_Expect_ThrowsIncorrectParameterException() {
        certificate.setDescription("");
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> CertificateValidator.validate(certificate));
    }

    @Test
    void When_PriceMoreThanThreeIntegerDigits_Expect_ThrowsIncorrectParameterException() {
        certificate.setPrice(new BigDecimal("1111.00"));
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> CertificateValidator.validate(certificate));
    }

    @Test
    void When_PriceMoreThanTwoFractionalDigits_Expect_ThrowsIncorrectParameterException() {
        certificate.setPrice(new BigDecimal("11.001"));
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> CertificateValidator.validate(certificate));
    }

    @Test
    void When_DurationMoreThanYear_Expect_ThrowsIncorrectParameterException() {
        certificate.setDuration((short) 366);
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> CertificateValidator.validate(certificate));
    }

    @Test
    void When_DurationLesserThanThreeDays_Expect_ThrowsIncorrectParameterException() {
        certificate.setDuration((short) 2);
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> CertificateValidator.validate(certificate));
    }
}