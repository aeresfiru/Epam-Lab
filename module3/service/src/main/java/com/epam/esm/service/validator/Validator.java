package com.epam.esm.service.validator;

import com.epam.esm.service.ExceptionResult;
import com.epam.esm.service.IncorrectParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Validator
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Slf4j
@Component
@PropertySource(value = "classpath:validation.properties")
public abstract class Validator<T> {

    public abstract void validateForCreate(T t) throws IncorrectParameterException;

    public abstract void validateForUpdate(T t) throws IncorrectParameterException;

    protected boolean isValidString(String name, int minLength, int maxLength) {
        return name != null
                && name.length() >= minLength
                && name.length() <= maxLength;
    }

    protected void throwExceptionIfEntityIsNotValid(ExceptionResult result) {
        if (!result.getExceptions().isEmpty()) {
            throw new IncorrectParameterException(result);
        }
    }
}
