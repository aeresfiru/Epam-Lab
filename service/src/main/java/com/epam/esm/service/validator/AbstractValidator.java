package com.epam.esm.service.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.Set;

public abstract class AbstractValidator<T> {

    protected final ValidatorFactory validatorFactory;

    protected final Validator validator;

    AbstractValidator() {
        validatorFactory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = validatorFactory.usingContext()
                .messageInterpolator(new ParameterMessageInterpolator())
                .getValidator();
    }

    public abstract Set<ConstraintViolation<T>> validate(T t);
}
