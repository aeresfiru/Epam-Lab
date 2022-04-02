package com.epam.esm.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CertificateUnitTest {

    ValidatorFactory validatorFactory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();

    Validator validator = validatorFactory.usingContext()
            .messageInterpolator(new ParameterMessageInterpolator())
            .getValidator();

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
    void whenCertificateFieldsAllGood_thenShouldNotGiveConstraintViolations() {
        Set<ConstraintViolation<Certificate>> violations = validator.validate(certificate);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenNameLessThanThreeCharactersLong_thenShouldGiveConstraintViolations() {
        certificate.setName("q");
        Set<ConstraintViolation<Certificate>> violations = validator
                .validate(certificate);
        assertEquals(1, violations.size());
        ConstraintViolation<Certificate> violation = violations.iterator().next();
        assertEquals("Name should be between 3 and 100 characters long", violation.getMessage());
    }

    @Test
    void whenNameMoreThan100CharsLong_thenShouldGiveConstraintViolations() {
        String space101 = new String(new char[101]).replace('\0', ' ');
        certificate.setName(space101);
        Set<ConstraintViolation<Certificate>> violations = validator
                .validate(certificate);
        assertEquals(1, violations.size());
        ConstraintViolation<Certificate> violation = violations.iterator().next();
        assertEquals("Name should be between 3 and 100 characters long", violation.getMessage());
    }

    @Test
    void whenEmptyName_thenShouldGiveConstraintViolations() {
        certificate.setName("");
        Set<ConstraintViolation<Certificate>> violations = validator
                .validate(certificate);
        assertEquals(2, violations.size());
    }

    @Test
    void whenDescLessThanTwentyCharsLong_thenShouldGiveConstraintViolations() {
        certificate.setDescription("Bad description");
        Set<ConstraintViolation<Certificate>> violations = validator
                .validate(certificate);
        assertEquals(1, violations.size());
        ConstraintViolation<Certificate> violation = violations.iterator().next();
        assertEquals("Description should be between 20 and 500 characters long", violation.getMessage());
    }

    @Test
    void whenDescMoreThan500CharsLong_thenShouldGiveConstraintViolations() {
        String space501 = new String(new char[501]).replace('\0', ' ');
        certificate.setDescription(space501);
        Set<ConstraintViolation<Certificate>> violations = validator
                .validate(certificate);
        assertEquals(1, violations.size());
        ConstraintViolation<Certificate> violation = violations.iterator().next();
        assertEquals("Description should be between 20 and 500 characters long", violation.getMessage());
    }

    @Test
    void whenEmptyDesc_thenShouldGiveConstraintViolations() {
        certificate.setDescription("");
        Set<ConstraintViolation<Certificate>> violations = validator
                .validate(certificate);
        assertEquals(2, violations.size());
    }

    @Test
    void whenPriceMoreThanThreeIntegerDigits_thenShouldGiveConstraintViolations() {
        certificate.setPrice(new BigDecimal("1111.00"));
        Set<ConstraintViolation<Certificate>> violations = validator.validate(certificate);
        assertEquals(1, violations.size());
        ConstraintViolation<Certificate> violation = violations.iterator().next();
        assertEquals("numeric value out of bounds (<3 digits>.<2 digits> expected)", violation.getMessage());
    }

    @Test
    void whenPriceMoreThanTwoFractionalDigits_thenShouldGiveConstraintViolations() {
        certificate.setPrice(new BigDecimal("11.001"));
        Set<ConstraintViolation<Certificate>> violations = validator.validate(certificate);
        assertEquals(1, violations.size());
        ConstraintViolation<Certificate> violation = violations.iterator().next();
        assertEquals("numeric value out of bounds (<3 digits>.<2 digits> expected)", violation.getMessage());
    }

    @Test
    void whenDurationMoreThanYear_thenShouldGiveConstraintViolations() {
        certificate.setDuration((short) 366);
        Set<ConstraintViolation<Certificate>> violations = validator.validate(certificate);
        assertEquals(1, violations.size());
        ConstraintViolation<Certificate> violation = violations.iterator().next();
        assertEquals("Duration should be lesser than year", violation.getMessage());
    }

    @Test
    void whenDurationLesserThanThreeDays_thenShouldGiveConstraintViolations() {
        certificate.setDuration((short) 2);
        Set<ConstraintViolation<Certificate>> violations = validator.validate(certificate);
        assertEquals(1, violations.size());
        ConstraintViolation<Certificate> violation = violations.iterator().next();
        assertEquals("Duration should be more than 3 days", violation.getMessage());
    }

    @Test
    void whenCreateDateIsNull_thenShouldGiveConstraintViolations() {
        certificate.setCreateDate(null);
        Set<ConstraintViolation<Certificate>> violations = validator.validate(certificate);
        assertEquals(1, violations.size());
    }

    @Test
    void whenLastUpdateDateIsNull_thenShouldGiveConstraintViolations() {
        certificate.setCreateDate(null);
        Set<ConstraintViolation<Certificate>> violations = validator.validate(certificate);
        assertEquals(1, violations.size());
    }

    @Test
    void whenTagsIsEmpty_thenShouldGiveConstraintViolations() {
        certificate.setTags(new HashSet<>());
        Set<ConstraintViolation<Certificate>> violations = validator.validate(certificate);
        assertEquals(1, violations.size());
    }
}