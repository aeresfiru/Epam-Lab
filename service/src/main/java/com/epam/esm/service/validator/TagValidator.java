package com.epam.esm.service.validator;

import com.epam.esm.domain.Tag;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ErrorConstraint;
import com.epam.esm.service.exception.IncorrectParameterException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("tagValidator")
public class TagValidator extends Validator<Tag> {
    private static final int MAX_LENGTH_NAME = 20;
    private static final int MIN_LENGTH_NAME = 3;

    public void validate(Tag tag) throws IncorrectParameterException {
        validateName(tag.getName());
    }

    public void validateName(String name) throws IncorrectParameterException {
        if (name == null
                || name.length() < MIN_LENGTH_NAME
                || name.length() > MAX_LENGTH_NAME) {
            throw new IncorrectParameterException(
                    ErrorConstraint.INVALID_TAG_NAME,
                    ErrorCode.TAG_ERROR);
        }
    }
}
