package com.epam.esm.service.validator;

import com.epam.esm.domain.Tag;
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
    void When_TagFieldsAllGood_Expect_NotGiveConstraintViolations() {
    }

    @Test
    void When_NameLessThanThreeCharactersLong_Expect_GiveConstraintViolations() {
        tag.setName("q");
    }

    @Test
    void When_NameMoreThan100CharactersLong_Expect_GiveConstraintViolations() {
        String space101 = new String(new char[101]).replace('\0', ' ');
    }
}