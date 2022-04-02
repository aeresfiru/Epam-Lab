package com.epam.esm.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TagUnitTest {
    ValidatorFactory validatorFactory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();

    Validator validator = validatorFactory.usingContext()
            .messageInterpolator(new ParameterMessageInterpolator())
            .getValidator();

    private Tag tag;

    @BeforeEach
    public void setupCertificate() {
        tag = Tag.builder()
                .id(101L)
                .name("Tag name")
                .build();
    }

    @Test
    void whenTagFieldsAllGood_thenShouldNotGiveConstraintViolations() {
        Set<ConstraintViolation<Tag>> violations = validator.validate(tag);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenNameLessThanThreeCharactersLong_thenShouldGiveConstraintViolations() {
        tag.setName("q");
        Set<ConstraintViolation<Tag>> violations = validator
                .validate(tag);
        assertEquals(1, violations.size());
        ConstraintViolation<Tag> violation = violations.iterator().next();
        assertEquals("Name should be between 3 and 100 characters long", violation.getMessage());
    }

    @Test
    void whenNameMoreThan100CharactersLong_thenShouldGiveConstraintViolations() {
        String space101 = new String(new char[101]).replace('\0', ' ');
        tag.setName(space101);
        Set<ConstraintViolation<Tag>> violations = validator
                .validate(tag);
        assertEquals(1, violations.size());
        ConstraintViolation<Tag> violation = violations.iterator().next();
        assertEquals("Name should be between 3 and 100 characters long", violation.getMessage());
    }
}