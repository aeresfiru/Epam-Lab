package com.epam.esm.service.validator;

import com.epam.esm.domain.Tag;
import com.epam.esm.service.validator.annotation.ValidTag;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidTagValidator implements ConstraintValidator<ValidTag, Tag> {

    @Override
    public boolean isValid(Tag value, ConstraintValidatorContext context) {
        return false;
    }
}
