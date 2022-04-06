package com.epam.esm.service.validator;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

class CertificateValidatorTest {

    private Certificate certificate;

    @BeforeEach
    public void setupCertificate() {
        certificate = Certificate.builder()
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
    void When_CertificateFieldsAllGood_Expect_NotGiveConstraintViolations() {
    }

    @Test
    void When_NameLessThanThreeCharactersLong_Expect_GiveConstraintViolations() {
    }

    @Test
    void When_NameMoreThan100CharsLong_Expect_GiveConstraintViolations() {
        String space101 = new String(new char[101]).replace('\0', ' ');
        certificate.setName(space101);
    }

    @Test
    void When_EmptyName_Expect_GiveConstraintViolations() {
        certificate.setName("");
    }

    @Test
    void When_DescLessThanTwentyCharsLong_Expect_GiveConstraintViolations() {
        certificate.setDescription("Bad description");
    }

    @Test
    void When_DescMoreThan500CharsLong_Expect_GiveConstraintViolations() {
        String space501 = new String(new char[501]).replace('\0', ' ');
    }

    @Test
    void When_EmptyDesc_Expect_GiveConstraintViolations() {
        certificate.setDescription("");
    }

    @Test
    void When_PriceMoreThanThreeIntegerDigits_Expect_GiveConstraintViolations() {
        certificate.setPrice(new BigDecimal("1111.00"));
    }

    @Test
    void When_PriceMoreThanTwoFractionalDigits_Expect_GiveConstraintViolations() {
        certificate.setPrice(new BigDecimal("11.001"));
    }

    @Test
    void When_DurationMoreThanYear_Expect_GiveConstraintViolations() {
        certificate.setDuration((short) 366);
    }

    @Test
    void When_DurationLesserThanThreeDays_Expect_GiveConstraintViolations() {
        certificate.setDuration((short) 2);
    }

    @Test
    void When_CreateDateIsNull_Expect_GiveConstraintViolations() {
        certificate.setCreateDate(null);
    }

    @Test
    void When_LastUpdateDateIsNull_Expect_GiveConstraintViolations() {
        certificate.setCreateDate(null);
    }

    @Test
    void When_TagsIsEmpty_Expect_GiveConstraintViolations() {
        certificate.setTags(new HashSet<>());
    }
}