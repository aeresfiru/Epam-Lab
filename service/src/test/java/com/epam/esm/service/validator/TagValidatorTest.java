package com.epam.esm.service.validator;

import com.epam.esm.domain.Tag;
import com.epam.esm.service.exception.IncorrectParameterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TagValidatorTest {

    private Tag tag;

    @BeforeEach
    public void setupCertificate() {
        tag = Tag.builder()
                .id(101L)
                .name("Tag name")
                .build();
    }

    @Test
    void When_TagFieldsAllGood_Expect_NotThrowsIncorrectParameterException() {
        Assertions.assertDoesNotThrow(() -> TagValidator.validate(tag));
    }

    @Test
    void When_NameLessThanThreeCharactersLong_Expect_ThrowsIncorrectParameterException() {
        tag.setName("q");
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> TagValidator.validate(tag));
    }

    @Test
    void When_NameMoreThan100CharactersLong_Expect_ThrowsIncorrectParameterException() {
        String spaces = new String(new char[101]).replace('\0', ' ');
        tag.setName(spaces);
        Assertions.assertThrows(IncorrectParameterException.class,
                () -> TagValidator.validate(tag));
    }
}