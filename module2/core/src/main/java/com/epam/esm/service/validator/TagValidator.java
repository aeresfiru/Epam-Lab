package com.epam.esm.service.validator;

import com.epam.esm.domain.Tag;
import com.epam.esm.service.exception.IncorrectParameterException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.epam.esm.service.exception.ErrorCode.INCORRECT_CERTIFICATE_PARAMETER;

@Component
public class TagValidator {

    private static final String REGEX = "[\\w\\W]{3,20}";

    private TagValidator() {
    }

    public static void validate(Tag tag) throws IncorrectParameterException {
        validateName(tag.getName());
    }

    public static void validateName(String name) throws IncorrectParameterException {
        if (StringUtils.isBlank(name)
                || !Pattern.compile(REGEX).matcher(name.trim()).find()) {
            throw new IncorrectParameterException("name", name, INCORRECT_CERTIFICATE_PARAMETER);
        }
    }
}